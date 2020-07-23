package com.khaniv.openalert.services;

import com.google.common.collect.Lists;
import com.khaniv.openalert.MatchDto;
import com.khaniv.openalert.checkers.CheckingUtils;
import com.khaniv.openalert.documents.Match;
import com.khaniv.openalert.documents.MissingPerson;
import com.khaniv.openalert.enums.MissingPersonType;
import com.khaniv.openalert.enums.OperatorMatchStatus;
import com.khaniv.openalert.enums.UserMatchStatus;
import com.khaniv.openalert.errors.exceptions.DocumentDuplicatesException;
import com.khaniv.openalert.errors.exceptions.DocumentNotFoundException;
import com.khaniv.openalert.errors.exceptions.MatchAlreadyExistsException;
import com.khaniv.openalert.mappers.MatchMapper;
import com.khaniv.openalert.repositories.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final MissingPersonService missingPersonService;
    private final MatchMapper matchMapper = Mappers.getMapper(MatchMapper.class);

    public MatchDto findById(UUID id) {
        return matchMapper.toDto(
                matchRepository.findById(id).orElseThrow(() -> new DocumentNotFoundException(Match.class, id))
        );
    }

    public List<MatchDto> findByLostPersonId(UUID lostPersonId) {
        return matchMapper.toDto(matchRepository.findByLostPersonId(lostPersonId));
    }

    public List<MatchDto> findBySeenPersonId(UUID seenPersonId) {
        return matchMapper.toDto(matchRepository.findBySeenPersonId(seenPersonId));
    }

    @Transactional
    public MatchDto save(MatchDto match) {
        checkMatch(match);
        MatchDto newMatch = generateMatch(match);
        return matchMapper.toDto(matchRepository.save(matchMapper.toDocument(newMatch)));
    }

    @Transactional
    public List<MatchDto> saveAll(List<MatchDto> matches) {
        checkNewMatches(matches);
        List<MatchDto> newMatches = matches.stream()
                .map(this::generateMatch).collect(Collectors.toList());
        return matchMapper.toDto(matchRepository.saveAll(matchMapper.toDocument(newMatches)));
    }

    @Transactional
    public void delete(UUID id) {
        matchRepository.deleteById(id);
    }

    @Transactional
    public MatchDto updateOperatorStatus(MatchDto match) {
        MatchDto originalMatch = findById(match.getId());
        originalMatch.setOperatorMatchStatus(match.getOperatorMatchStatus());
        return matchMapper.toDto(matchRepository.save(matchMapper.toDocument(originalMatch)));
    }

    @Transactional
    public MatchDto updateUserStatus(MatchDto match) {
        MatchDto originalMatch = findById(match.getId());
        originalMatch.setUserMatchStatus(match.getUserMatchStatus());
        return matchMapper.toDto(matchRepository.save(matchMapper.toDocument(originalMatch)));
    }

    @Transactional
    public MatchDto updateActive(UUID id, boolean active) {
        MatchDto originalMatch = findById(id);
        originalMatch.setActive(active);
        return matchMapper.toDto(matchRepository.save(matchMapper.toDocument(originalMatch)));
    }

    @Transactional
    public MatchDto viewedByUser(UUID id) {
        MatchDto match = findById(id);
        match.setViewedByUser(true);
        return matchMapper.toDto(matchRepository.save(matchMapper.toDocument(match)));
    }

    @Transactional
    public MatchDto viewedByOperator(UUID id) {
        MatchDto match = findById(id);
        match.setViewedByOperator(true);
        return matchMapper.toDto(matchRepository.save(matchMapper.toDocument(match)));
    }

    public List<MatchDto> findAll(List<MatchDto> matches) {
        checkMatchesCount(matches);
        return matchMapper.toDto(Lists.newArrayList(matchRepository.findAllById(matches.stream()
                .map(MatchDto::getId)
                .collect(Collectors.toList()))));
    }

    private MatchDto generateMatch(MatchDto match) {
        return MatchDto.builder()
                .lostPersonId(match.getLostPersonId())
                .seenPersonId(match.getSeenPersonId())
                .probability(match.getProbability())
                .viewedByOperator(false)
                .viewedByUser(false)
                .operatorMatchStatus(OperatorMatchStatus.ACTIVE)
                .userMatchStatus(UserMatchStatus.NOT_CONFIRMED)
                .build();
    }

    private void checkMatch(MatchDto match) {
        checkMatchPeopleExist(match);
        checkMatchPeopleExistByIdAndType(match);
        checkMatchNotExistsAlready(match);
    }

    private void checkMatchPeopleExist(MatchDto match) {
        Set<UUID> ids = new HashSet<>();
        if (!missingPersonService.existsById(match.getSeenPersonId()))
            ids.add(match.getSeenPersonId());

        if (!missingPersonService.existsById(match.getLostPersonId()))
            ids.add(match.getLostPersonId());

        if (!ids.isEmpty())
            throw new DocumentNotFoundException(MissingPerson.class, ids);
    }

    private void checkMatchPeopleExistByIdAndType(MatchDto match) {
        List<String> messages = new ArrayList<>();

        if (!missingPersonService.existsByIdAndType(match.getLostPersonId(), MissingPersonType.LOST))
            messages.add("Lost person not found by ID " + match.getLostPersonId() + "!");

        if (!missingPersonService.existsByIdAndType(match.getSeenPersonId(), MissingPersonType.SEEN))
            messages.add("Seen person not found by ID " + match.getSeenPersonId() + "!");

        if (!messages.isEmpty()) {
            throw new DocumentNotFoundException(String.join("\r\n", messages));
        }
    }

    private void checkMatchNotExistsAlready(MatchDto match) {
        if (matchRepository.findByLostPersonIdAndSeenPersonId(match.getLostPersonId(), match.getSeenPersonId()).isPresent())
            throw new MatchAlreadyExistsException(match.getLostPersonId(), match.getSeenPersonId());
    }

    private void checkNewMatches(List<MatchDto> matches) {
        checkMatchesCount(matches);
        matches.forEach(this::checkMatch);
        long uniqueMatchesCount = matches.stream()
                .map(m -> Match.builder()
                        .lostPersonId(m.getLostPersonId())
                        .seenPersonId(m.getSeenPersonId())
                        .probability(0.0)
                        .build())
                .distinct()
                .count();

        if (uniqueMatchesCount != matches.size())
            throw new DocumentDuplicatesException(Match.class);
    }

    private void checkMatchesCount(List<MatchDto> matches) {
        CheckingUtils.checkMaxSize(matchMapper.toDocument(matches), Match.class);
    }
}
