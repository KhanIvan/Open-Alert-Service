package com.khaniv.openalert.documents;

import com.khaniv.openalert.data.Match;
import com.khaniv.openalert.data.PersonDescription;
import com.khaniv.openalert.enums.PersonType;
import com.khaniv.openalert.enums.SearchStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Document
@Data
@Builder
public class Person {
    @Id
    private UUID id;
    @Indexed
    private PersonType type;

    //status
    @Indexed
    private Boolean active;
    private SearchStatus status;
    private Map<UUID, Match> matches;
    private LocalDateTime foundAt;

    //description
    private PersonDescription description;

    //other information
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String details;
}
