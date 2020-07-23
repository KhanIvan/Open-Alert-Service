package com.khaniv.openalert;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
public class BaseDto {
    private UUID id;
    private Boolean active;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastModifiedAt;
}
