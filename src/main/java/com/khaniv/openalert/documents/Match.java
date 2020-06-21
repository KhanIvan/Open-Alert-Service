package com.khaniv.openalert.documents;

import com.khaniv.openalert.documents.enums.OperatorMatchStatus;
import com.khaniv.openalert.documents.enums.UserMatchStatus;
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
public class Match extends BaseDocument {
    @Id
    private UUID id;
    @Indexed
    @NotNull
    private UUID lostPersonId;
    @Indexed
    @NotNull
    private UUID seenPersonId;

    private double probability;
    private OperatorMatchStatus operatorMatchStatus;
    private UserMatchStatus userMatchStatus;
    private Boolean viewedByUser;
    private Boolean viewedByOperator;
    private Boolean active;
}
