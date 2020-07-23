package com.khaniv.openalert.data;

import com.khaniv.openalert.enums.PersonSex;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MissingPersonDescription {
    private String name;
    private PersonSex sex;
    private LocalDateTime birthDate;
    private float height;
    private float weight;
    private String citizenship;
    private String nationality;
    private String skinColor;
    private String hairColor;
    private String eyesColor;
    private String distinctiveFeatures;
    private String otherDetails;
}
