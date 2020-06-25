package com.khaniv.openalert.unit.service;

import com.khaniv.openalert.documents.MissingPerson;
import com.khaniv.openalert.documents.data.MissingPersonDescription;
import com.khaniv.openalert.documents.data.MissingPersonStatus;
import com.khaniv.openalert.documents.enums.MissingPersonType;
import com.khaniv.openalert.documents.enums.PersonSex;
import com.khaniv.openalert.documents.enums.SearchStatus;
import com.khaniv.openalert.repositories.MissingPersonRepository;
import com.khaniv.openalert.services.MissingPersonService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MissingPersonServiceTest {
    private MissingPersonService missingPersonService;

    @Mock
    private MissingPersonRepository missingPersonRepository;

    @Before
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
        missingPersonService = new MissingPersonService(missingPersonRepository);
    }

    @Test
    public void testFindById() {
        when(missingPersonRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(new MissingPerson()));

        MissingPerson missingPerson = missingPersonService.findById(UUID.randomUUID());
        Assert.assertNotNull(missingPerson);
    }

    @Test
    public void testFindByTypeAndActive() {
        when(missingPersonRepository.findByTypeAndActive(Mockito.any(MissingPersonType.class),
                Mockito.anyBoolean())).thenReturn(Collections.singletonList(new MissingPerson()));

        List<MissingPerson> missingPeople = missingPersonService.findByTypeAndActive(MissingPersonType.SEEN, true);
        Assert.assertNotNull(missingPeople);
        Assert.assertEquals(1, missingPeople.size());
    }

    @Test
    public void testSave() {
        when(missingPersonRepository.save(Mockito.any(MissingPerson.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        MissingPerson missingPerson = generateMissingPerson();
        MissingPerson savedPerson = missingPersonService.save(missingPerson);
        assertMissingPerson(savedPerson);
        Assert.assertEquals(missingPerson.getType(), savedPerson.getType());
        Assert.assertEquals(missingPerson.getDescription(), savedPerson.getDescription());
        Assert.assertEquals(missingPerson.getStatus().getLostAt(), savedPerson.getStatus().getLostAt());
        Assert.assertEquals(SearchStatus.LOST, savedPerson.getStatus().getStatus());
        Assert.assertTrue(savedPerson.getActive());
    }

    @Test
    public void testUpdateDescription() {
        mockUpdateMethods();
        MissingPerson missingPerson = generateUpdatedMissingPerson();
        MissingPerson modifiedMissingPerson = missingPersonService.updateDescription(missingPerson);
        assertMissingPerson(modifiedMissingPerson);
        Assert.assertEquals(missingPerson.getDescription(), modifiedMissingPerson.getDescription());
        Assert.assertNotEquals(missingPerson.getStatus(), modifiedMissingPerson.getStatus());
        Assert.assertNotEquals(missingPerson.getType(), modifiedMissingPerson.getType());
    }

    @Test
    public void testUpdateStatus() {
        mockUpdateMethods();
        MissingPerson missingPerson = generateUpdatedMissingPerson();
        MissingPerson modifiedMissingPerson = missingPersonService.updateStatus(missingPerson);
        assertMissingPerson(modifiedMissingPerson);
        Assert.assertNotEquals(missingPerson.getDescription(), modifiedMissingPerson.getDescription());
        Assert.assertEquals(missingPerson.getStatus(), modifiedMissingPerson.getStatus());
        Assert.assertNotEquals(missingPerson.getType(), modifiedMissingPerson.getType());
    }

    @Test
    public void testUpdateActive() {
        mockUpdateMethods();
        MissingPerson missingPerson = missingPersonService.updateActive(UUID.randomUUID(), false);
        assertMissingPerson(missingPerson);
        Assert.assertFalse(missingPerson.getActive());
    }

    @Test
    public void testExistsById() {
        when(missingPersonRepository.existsById(Mockito.any(UUID.class)))
                .thenReturn(true);

        Assert.assertTrue(missingPersonService.existsById(UUID.randomUUID()));
    }

    @Test
    public void testDelete() {
        missingPersonService.delete(UUID.randomUUID());
        verify(missingPersonRepository, times(1)).deleteById(Mockito.any(UUID.class));

    }

    private void mockUpdateMethods() {
        when(missingPersonRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(generateMissingPerson()));

        when(missingPersonRepository.save(Mockito.any(MissingPerson.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
    }

    private void assertMissingPerson(MissingPerson missingPerson) {
        Assert.assertNotNull(missingPerson);
        Assert.assertNotNull(missingPerson.getId());
        Assert.assertNotNull(missingPerson.getDescription());
        Assert.assertNotNull(missingPerson.getStatus());
        Assert.assertNotNull(missingPerson.getType());
        Assert.assertNotNull(missingPerson.getActive());
        Assert.assertNotNull(missingPerson.getStatus().getLostAt());
        Assert.assertNotNull(missingPerson.getStatus().getStatus());
        Assert.assertNotNull(missingPerson.getDescription().getBirthDate());
        Assert.assertNotNull(missingPerson.getDescription().getCitizenship());
        Assert.assertNotNull(missingPerson.getDescription().getDistinctiveFeatures());
        Assert.assertNotNull(missingPerson.getDescription().getEyesColor());
        Assert.assertNotNull(missingPerson.getDescription().getHairColor());
        Assert.assertNotNull(missingPerson.getDescription().getName());
        Assert.assertNotNull(missingPerson.getDescription().getNationality());
        Assert.assertNotNull(missingPerson.getDescription().getSex());
        Assert.assertNotNull(missingPerson.getDescription().getSkinColor());
        Assert.assertNotNull(missingPerson.getDescription().getOtherDetails());
    }

    private MissingPerson generateNewMissingPerson() {
        return MissingPerson.builder()
                .description(MissingPersonDescription.builder()
                        .birthDate(LocalDateTime.of(1992, 7, 18, 0, 0))
                        .citizenship("Russia")
                        .distinctiveFeatures("Dragon tattoo on left shoulder")
                        .eyesColor("Brown")
                        .hairColor("Ginger")
                        .height(180)
                        .name("Alexandr Romanenkov")
                        .nationality("Russian")
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

    private MissingPerson generateMissingPerson() {
        return MissingPerson.builder()
                .id(UUID.randomUUID())
                .active(true)
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
                        .status(SearchStatus.LOST)
                        .build())
                .build();
    }

    private MissingPerson generateUpdatedMissingPerson() {
        return MissingPerson.builder()
                .id(UUID.randomUUID())
                .active(true)
                .description(MissingPersonDescription.builder()
                        .birthDate(LocalDateTime.of(1993, 1, 23, 0, 0))
                        .citizenship("Australia")
                        .distinctiveFeatures("None")
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


    @Test(expected = IllegalArgumentException.class)
    public void personShouldNotExits() {
        when(missingPersonRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        missingPersonService.findById(UUID.randomUUID());
    }
}
