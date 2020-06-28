package com.khaniv.openalert.generators;

import com.khaniv.openalert.documents.Match;
import com.khaniv.openalert.documents.enums.OperatorMatchStatus;
import com.khaniv.openalert.documents.enums.UserMatchStatus;
import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * @author Ivan Khan
 */

@UtilityClass
public class MatchGenerator {
    public Match generateNewMatch() {
        return Match.builder()
                .seenPersonId(UUID.randomUUID())
                .lostPersonId(UUID.randomUUID())
                .probability(0.5)
                .build();
    }

    public Match generateMatch() {
        return Match.builder()
                .id(UUID.randomUUID())
                .probability(0.5)
                .lostPersonId(UUID.randomUUID())
                .seenPersonId(UUID.randomUUID())
                .userMatchStatus(UserMatchStatus.NOT_CONFIRMED)
                .operatorMatchStatus(OperatorMatchStatus.ACTIVE)
                .active(true)
                .viewedByOperator(false)
                .viewedByUser(false)
                .build();
    }

    public Match generateModifiedStatusesMatch() {
        Match match = generateMatch();
        match.setUserMatchStatus(UserMatchStatus.CONFIRMED);
        match.setOperatorMatchStatus(OperatorMatchStatus.PROBABLE);
        return match;
    }
}
