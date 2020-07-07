package com.khaniv.openalert.documents;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.UUID;

@Document
@Data
@SuperBuilder
@NoArgsConstructor
public class BaseDocument {
    @Id
    private UUID id;
    private Boolean active;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastModifiedAt;
}
