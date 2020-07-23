package com.khaniv.openalert;

import com.khaniv.openalert.data.MissingPersonDescription;
import com.khaniv.openalert.data.MissingPersonStatus;
import com.khaniv.openalert.enums.MissingPersonType;
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
