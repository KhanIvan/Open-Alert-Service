package com.khaniv.openalert.documents;

import com.khaniv.openalert.documents.data.MissingPersonDescription;
import com.khaniv.openalert.documents.data.MissingPersonStatus;
import com.khaniv.openalert.documents.enums.MissingPersonType;
import com.sun.istack.internal.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissingPerson extends BaseDocument {
    @Id
    private UUID id;
    @Indexed
    private MissingPersonType type;
    @Indexed
    private Boolean active;

    @NotNull
    private MissingPersonStatus status;
    private MissingPersonDescription description;
}
