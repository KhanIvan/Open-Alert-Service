package com.khaniv.openalert.helpers.generators;

import com.khaniv.openalert.data.MissingPersonDescription;
import com.khaniv.openalert.data.MissingPersonStatus;
import com.khaniv.openalert.documents.MissingPerson;
import com.khaniv.openalert.enums.MissingPersonType;
import com.khaniv.openalert.enums.PersonSex;
import com.khaniv.openalert.enums.SearchStatus;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Ivan Khan
 */

@UtilityClass
public class MissingPersonGenerator {
    public MissingPerson generateNewMissingPerson() {
        return MissingPerson.builder()
                .description(MissingPersonDescription.builder()
                        .birthDate(LocalDateTime.of(1992, 7, 18, 0, 0))
                        .citizenship("UK")
                        .distinctiveFeatures("Dragon tattoo on left shoulder")
                        .eyesColor("Green")
                        .hairColor("Brown")
                        .height(180)
                        .name("John Doe")
                        .nationality("Irish")
                        .sex(PersonSex.MALE)
                        .skinColor("White")
                        .weight(78)
                        .otherDetails("Went out to buy cigarettes and never returned")
                        .build())
                .type(MissingPersonType.LOST)
                .status(MissingPersonStatus.builder()
                        .lostAt(ZonedDateTime.now())
                        .build())
                .build();
    }

    public MissingPerson generateMissingPerson() {
        MissingPerson missingPerson = generateNewMissingPerson();
        missingPerson.setId(UUID.randomUUID());
        missingPerson.setActive(true);
        missingPerson.getStatus().setStatus(SearchStatus.LOST);
        return missingPerson;
    }

    public MissingPerson generateUpdatedMissingPerson() {
        return MissingPerson.builder()
                .id(UUID.randomUUID())
                .active(true)
                .description(MissingPersonDescription.builder()
                        .birthDate(LocalDateTime.of(1993, 1, 23, 0, 0))
                        .citizenship("Australia")
                        .distinctiveFeatures("Birthmark on neck")
                        .eyesColor("Blue")
                        .hairColor("Brown")
                        .height(173)
                        .name("Jane Doe")
                        .nationality("Australian")
                        .sex(PersonSex.FEMALE)
                        .skinColor("Black")
                        .weight(65)
                        .otherDetails("Lost on the way to the job")
                        .build())
                .type(MissingPersonType.SEEN)
                .status(MissingPersonStatus.builder()
                        .lostAt(ZonedDateTime.now())
                        .status(SearchStatus.FOUND)
                        .foundAt(ZonedDateTime.now())
                        .build())
                .build();
    }

}
