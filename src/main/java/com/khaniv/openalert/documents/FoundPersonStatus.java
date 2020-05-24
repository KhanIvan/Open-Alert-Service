package com.khaniv.openalert.documents;

import com.khaniv.openalert.documents.enums.FoundStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class FoundPersonStatus {
    private boolean active;
    private FoundStatus status;
    private List<UUID> closeMatches;
    private List<UUID> presumablyFoundIds;
    private UUID foundId;
    private LocalDateTime foundAt;
}
