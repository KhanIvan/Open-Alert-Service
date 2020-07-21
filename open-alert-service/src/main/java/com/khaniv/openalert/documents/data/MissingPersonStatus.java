package com.khaniv.openalert.documents.data;

import com.khaniv.openalert.documents.enums.SearchStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.ZonedDateTime;

@Data
@Builder
public class MissingPersonStatus {
    private SearchStatus status;
    @NonNull
    private ZonedDateTime lostAt;
    private ZonedDateTime foundAt;
}
