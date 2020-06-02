package com.khaniv.openalert.documents;

import com.khaniv.openalert.documents.data.MissingPersonDescription;
import com.khaniv.openalert.documents.data.MissingPersonStatus;
import com.khaniv.openalert.documents.enums.MissingPersonType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.UUID;

@Document
@Data
@Builder
public class MissingPerson {
    @Id
    private UUID id;

    @Indexed
    private MissingPersonType type;

    @Indexed
    private Boolean active;

    private MissingPersonStatus status;
    private MissingPersonDescription description;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
