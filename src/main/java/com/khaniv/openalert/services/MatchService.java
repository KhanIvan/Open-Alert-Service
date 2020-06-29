package com.khaniv.openalert.services;

import com.google.common.collect.Lists;
import com.khaniv.openalert.documents.Match;
import com.khaniv.openalert.documents.MissingPerson;
import com.khaniv.openalert.documents.enums.OperatorMatchStatus;
import com.khaniv.openalert.documents.enums.UserMatchStatus;
import com.khaniv.openalert.errors.DocumentNotFoundException;
import com.khaniv.openalert.errors.MatchAlreadyExistsException;
import com.khaniv.openalert.repositories.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final MissingPersonService missingPersonService;
    public final static int MAX_COUNT = 100000;
    private final static String MAX_COUNT_ERROR = "Matches count is too big! Maximum count allowed: " + MAX_COUNT;

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
        checkMatchesCount(matches);
        List<Match> newMatches = matches.stream()
                .map(m -> {
                    checkMatch(m);
                    return generateMatch(m);
                }).collect(Collectors.toList());
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
        if (ids.size() > MAX_COUNT)
            throw new IllegalArgumentException(MAX_COUNT_ERROR);

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
                .id(UUID.randomUUID())
                .active(true)
                .lostPersonId(match.getLostPersonId())
                .seenPersonId(match.getSeenPersonId())
                .viewedByOperator(false)
                .viewedByUser(false)
                .operatorMatchStatus(OperatorMatchStatus.ACTIVE)
                .userMatchStatus(UserMatchStatus.NOT_CONFIRMED)
                .build();
    }

    private void checkMatch(Match match) {
        Set<UUID> ids = new HashSet<>();
        if (!missingPersonService.existsById(match.getSeenPersonId()))
            ids.add(match.getSeenPersonId());

        if (!missingPersonService.existsById(match.getLostPersonId()))
            ids.add(match.getLostPersonId());

        if (!ids.isEmpty())
            throw new DocumentNotFoundException(MissingPerson.class, ids);

        if (matchRepository.findByLostPersonIdAndSeenPersonId(match.getLostPersonId(), match.getSeenPersonId()).isPresent())
            throw new MatchAlreadyExistsException(match.getLostPersonId(), match.getSeenPersonId());
    }

    private void checkMatchesCount(List<Match> matches) {
        if (matches.size() > MAX_COUNT)
            throw new IllegalArgumentException(MAX_COUNT_ERROR);
    }
}
