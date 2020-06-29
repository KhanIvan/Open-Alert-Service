package com.khaniv.openalert.documents;

import com.khaniv.openalert.documents.data.MissingPersonDescription;
import com.khaniv.openalert.documents.data.MissingPersonStatus;
import com.khaniv.openalert.documents.enums.MissingPersonType;
import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MissingPerson extends BaseDocument {
    @Indexed
    private MissingPersonType type;

    @NotNull
    private MissingPersonStatus status;
    private MissingPersonDescription description;
}
