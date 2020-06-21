package com.khaniv.openalert.documents;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Document
@Data
public class BaseDocument {
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
