package com.khaniv.openalert.dto.data;

import com.khaniv.openalert.dto.enums.SearchStatus;
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
