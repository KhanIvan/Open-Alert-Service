package com.khaniv.openalert.documents;

import com.khaniv.openalert.documents.enums.PersonSex;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PersonDescription {
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
}
