package com.khaniv.openalert.services;

import com.khaniv.openalert.documents.Match;
import com.khaniv.openalert.documents.enums.OperatorMatchStatus;
import com.khaniv.openalert.documents.enums.UserMatchStatus;
import com.khaniv.openalert.repositories.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;

    public Match findById(UUID id) {
        return matchRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Match with ID " + id
                + " not found!"));
    }

    public List<Match> findByLostPersonId(UUID lostPersonId) {
        return matchRepository.findByLostPersonId(lostPersonId);
    }

    public List<Match> findBySeenPersonId(UUID seenPersonId) {
        return matchRepository.findBySeenPersonId(seenPersonId);
    }

    @Transactional
    public Match save(Match match) {
        if (matchRepository.findByLostPersonIdAndSeenPersonId(match.getLostPersonId(), match.getSeenPersonId()).isPresent())
            throw new IllegalArgumentException("Match between lost person" + match.getLostPersonId() +
                    " and seen person " + match.getSeenPersonId() + " already exists!");

        Match newMatch = generateMatch(match);
        return matchRepository.save(newMatch);
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
}
