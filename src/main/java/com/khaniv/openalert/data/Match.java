package com.khaniv.openalert.data;

import com.khaniv.openalert.enums.MatchStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Match {
    private UUID id;
    private MatchStatus status;
}
