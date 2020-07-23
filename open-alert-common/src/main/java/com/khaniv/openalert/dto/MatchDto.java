package com.khaniv.openalert.dto;

import com.khaniv.openalert.dto.enums.OperatorMatchStatus;
import com.khaniv.openalert.dto.enums.UserMatchStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class MatchDto extends BaseDto {
    @NonNull
    private UUID lostPersonId;
    @NonNull
    private UUID seenPersonId;
    @NonNull
    private Double probability;
    private OperatorMatchStatus operatorMatchStatus;
    private UserMatchStatus userMatchStatus;
    private Boolean viewedByUser;
    private Boolean viewedByOperator;
}
