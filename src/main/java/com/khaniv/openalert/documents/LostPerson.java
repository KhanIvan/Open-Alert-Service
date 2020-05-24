package com.khaniv.openalert.documents;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document
public class LostPerson extends AbstractPerson {
    private LostPersonStatus status;
}
