package com.khaniv.openalert.documents;

import com.khaniv.openalert.dto.data.MissingPersonDescription;
import com.khaniv.openalert.dto.data.MissingPersonStatus;
import com.khaniv.openalert.dto.enums.MissingPersonType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document
@SuperBuilder
@NoArgsConstructor
public class MissingPerson extends BaseDocument {
    @Indexed
    @NonNull
    private MissingPersonType type;

    @NonNull
    private MissingPersonStatus status;
    @NonNull
    private MissingPersonDescription description;
}
