package com.khaniv.openalert.documents;

import com.khaniv.openalert.documents.enums.OperatorMatchStatus;
import com.khaniv.openalert.documents.enums.UserMatchStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    @Id
    private UUID id;

    @Indexed
    private UUID lostPersonId;

    @Indexed
    private UUID detectedPersonId;

    private OperatorMatchStatus operatorMatchStatus;
    private UserMatchStatus userMatchStatus;
    private Boolean active;

    private Boolean seenByUser;
    private Boolean seenByOperator;
}
