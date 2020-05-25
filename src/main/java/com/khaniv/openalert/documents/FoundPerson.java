package com.khaniv.openalert.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class FoundPerson extends AbstractPerson {
    private FoundPersonStatus status;
}
