package com.khaniv.openalert.services;

import com.google.common.collect.Lists;
import com.khaniv.openalert.checkers.CheckingUtils;
import com.khaniv.openalert.documents.Match;
import com.khaniv.openalert.documents.MissingPerson;
import com.khaniv.openalert.documents.enums.MissingPersonType;
import com.khaniv.openalert.documents.enums.OperatorMatchStatus;
import com.khaniv.openalert.documents.enums.UserMatchStatus;
import com.khaniv.openalert.errors.exceptions.DocumentDuplicatesException;
import com.khaniv.openalert.errors.exceptions.DocumentNotFoundException;
import com.khaniv.openalert.errors.exceptions.MatchAlreadyExistsException;
import com.khaniv.openalert.repositories.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final MissingPersonService missingPersonService;

    public Match findById(UUID id) {
        return matchRepository.findById(id).orElseThrow(() -> new DocumentNotFoundException(Match.class, id));
    }

    public List<Match> findByLostPersonId(UUID lostPersonId) {
        return matchRepository.findByLostPersonId(lostPersonId);
    }

    public List<Match> findBySeenPersonId(UUID seenPersonId) {
        return matchRepository.findBySeenPersonId(seenPersonId);
    }

    @Transactional
    public Match save(Match match) {
        checkMatch(match);
        Match newMatch = generateMatch(match);
        return matchRepository.save(newMatch);
    }

    @Transactional
    public List<Match> saveAll(List<Match> matches) {
        checkNewMatches(matches);
        List<Match> newMatches = matches.stream()
                .map(this::generateMatch).collect(Collectors.toList());
        return matchRepository.saveAll(newMatches);
    }

    @Transactional
    public void delete(UUID id) {
        matchRepository.deleteById(id);
    }

    @Transactional
    public Match updateOperatorStatus(Match match) {
        Match originalMatch = findById(match.getId());
        originalMatch.setOperatorMatchStatus(match.getOperatorMatchStatus());
        return matchRepository.save(originalMatch);
    }

    @Transactional
    public Match updateUserStatus(Match match) {
        Match originalMatch = findById(match.getId());
        originalMatch.setUserMatchStatus(match.getUserMatchStatus());
        return matchRepository.save(originalMatch);
    }

    @Transactional
    public Match updateActive(UUID id, boolean active) {
        Match originalMatch = findById(id);
        originalMatch.setActive(active);
        return matchRepository.save(originalMatch);
    }

    @Transactional
    public Match viewedByUser(UUID id) {
        Match match = findById(id);
        match.setViewedByUser(true);
        return matchRepository.save(match);
    }

    @Transactional
    public Match viewedByOperator(UUID id) {
        Match match = findById(id);
        match.setViewedByOperator(true);
        return matchRepository.save(match);
    }

    @Transactional
    public List<Match> updateAllOperatorStatuses(List<Match> matches) {
        List<Match> modifiedMatches = findAll(matches);
        modifiedMatches.forEach(match -> match.setOperatorMatchStatus(matches.stream()
                .filter(m -> m.getId().equals(match.getId()))
                .findAny().get().getOperatorMatchStatus()));

        return matchRepository.saveAll(modifiedMatches);
    }

    @Transactional
    public List<Match> updateAllUserStatuses(List<Match> matches) {
        List<Match> modifiedMatches = findAll(matches);
        modifiedMatches.forEach(match -> match.setUserMatchStatus(matches.stream()
                .filter(m -> m.getId().equals(match.getId()))
                .findAny().get().getUserMatchStatus()));

        return matchRepository.saveAll(modifiedMatches);
    }

    @Transactional
    public List<Match> updateAllActive(List<UUID> ids, boolean active) {
        List<Match> matches = findAllByIds(ids);
        matches.forEach(match -> match.setActive(active));
        return matchRepository.saveAll(matches);
    }

    @Transactional
    public List<Match> updateAllViewedByUser(List<UUID> ids) {
        List<Match> matches = findAllByIds(ids);
        matches.forEach(match -> match.setViewedByUser(true));
        return matchRepository.saveAll(matches);
    }

    @Transactional
    public List<Match> updateAllViewedByOperator(List<UUID> ids) {
        List<Match> matches = findAllByIds(ids);
        matches.forEach(match -> match.setViewedByOperator(true));
        return matchRepository.saveAll(matches);
    }

    public List<Match> findAllByIds(List<UUID> ids) {
        CheckingUtils.checkMaxSize(ids, Match.class);

        return Lists.newArrayList(matchRepository.findAllById(ids));
    }

    public List<Match> findAll(List<Match> matches) {
        checkMatchesCount(matches);
        return Lists.newArrayList(matchRepository.findAllById(matches.stream()
                .map(Match::getId)
                .collect(Collectors.toList())));
    }

    private Match generateMatch(Match match) {
        return Match.builder()
                .lostPersonId(match.getLostPersonId())
                .seenPersonId(match.getSeenPersonId())
                .probability(match.getProbability())
                .viewedByOperator(false)
                .viewedByUser(false)
                .operatorMatchStatus(OperatorMatchStatus.ACTIVE)
                .userMatchStatus(UserMatchStatus.NOT_CONFIRMED)
                .build();
    }

    private void checkMatch(Match match) {
        checkMatchPeopleExist(match);
        checkMatchPeopleExistByIdAndType(match);
        checkMatchNotExistsAlready(match);
    }

    private void checkMatchPeopleExist(Match match) {
        Set<UUID> ids = new HashSet<>();
        if (!missingPersonService.existsById(match.getSeenPersonId()))
            ids.add(match.getSeenPersonId());

        if (!missingPersonService.existsById(match.getLostPersonId()))
            ids.add(match.getLostPersonId());

        if (!ids.isEmpty())
            throw new DocumentNotFoundException(MissingPerson.class, ids);
    }

    private void checkMatchPeopleExistByIdAndType(Match match) {
        List<String> messages = new ArrayList<>();

        if (!missingPersonService.existsByIdAndType(match.getLostPersonId(), MissingPersonType.LOST))
            messages.add("Lost person not found by ID " + match.getLostPersonId() + "!");

        if (!missingPersonService.existsByIdAndType(match.getSeenPersonId(), MissingPersonType.SEEN))
            messages.add("Seen person not found by ID " + match.getSeenPersonId() + "!");

        if (!messages.isEmpty()) {
            throw new DocumentNotFoundException(String.join("\r\n", messages));
        }
    }

    private void checkMatchNotExistsAlready(Match match) {
        if (matchRepository.findByLostPersonIdAndSeenPersonId(match.getLostPersonId(), match.getSeenPersonId()).isPresent())
            throw new MatchAlreadyExistsException(match.getLostPersonId(), match.getSeenPersonId());
    }

    private void checkNewMatches(List<Match> matches) {
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

    private void checkMatchesCount(List<Match> matches) {
        CheckingUtils.checkMaxSize(matches, Match.class);
    }
}
