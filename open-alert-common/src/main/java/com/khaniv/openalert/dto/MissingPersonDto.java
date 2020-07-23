package com.khaniv.openalert.dto;

import com.khaniv.openalert.dto.data.MissingPersonDescription;
import com.khaniv.openalert.dto.data.MissingPersonStatus;
import com.khaniv.openalert.dto.enums.MissingPersonType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class MissingPersonDto extends BaseDto {
    @NonNull
    private MissingPersonType type;
    @NonNull
    private MissingPersonStatus status;
    @NonNull
    private MissingPersonDescription description;
}
