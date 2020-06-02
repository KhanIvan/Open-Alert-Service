package com.khaniv.openalert.documents.data;

import com.khaniv.openalert.documents.enums.SearchStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
public class MissingPersonStatus {
    private SearchStatus status;
    private String disappearanceDetails;
    private ZonedDateTime lostAt;
    private ZonedDateTime foundAt;
}
