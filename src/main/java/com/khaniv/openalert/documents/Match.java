package com.khaniv.openalert.documents;

import com.khaniv.openalert.documents.enums.OperatorMatchStatus;
import com.khaniv.openalert.documents.enums.UserMatchStatus;
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
public class Match extends BaseDocument {
    @Id
    private UUID id;
    @Indexed
    private UUID lostPersonId;
    @Indexed
    private UUID seenPersonId;

    private Boolean active;

    private OperatorMatchStatus operatorMatchStatus;
    private UserMatchStatus userMatchStatus;

    private Boolean viewedByUser;
    private Boolean viewedByOperator;
}
