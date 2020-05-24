package com.khaniv.openalert.documents;

import com.khaniv.openalert.documents.enums.SearchStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class LostPersonStatus {
    private SearchStatus status;
    private Boolean active;
    private List<UUID> closeMatches;
    private List<UUID> presumablyFoundIds;
    private List<UUID> foundIds;
    private LocalDateTime foundAt;
}
