package com.khaniv.openalert.unit;

import com.khaniv.openalert.documents.MissingPerson;
import com.khaniv.openalert.documents.enums.MissingPersonType;
import com.khaniv.openalert.documents.enums.SearchStatus;
import com.khaniv.openalert.errors.DocumentNotFoundException;
import com.khaniv.openalert.generators.MissingPersonGenerator;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Unit test for {@link MissingPersonService}
 *
 * @author Ivan Khan
 */

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

        MissingPerson missingPerson = MissingPersonGenerator.generateNewMissingPerson();
        MissingPerson savedPerson = missingPersonService.save(missingPerson);
        assertMissingPerson(savedPerson);
        Assert.assertEquals(missingPerson.getType(), savedPerson.getType());
        Assert.assertEquals(missingPerson.getDescription(), savedPerson.getDescription());
        Assert.assertEquals(missingPerson.getStatus().getLostAt(), savedPerson.getStatus().getLostAt());
        Assert.assertEquals(SearchStatus.LOST, savedPerson.getStatus().getStatus());
    }

    @Test
    public void testUpdateDescription() {
        mockUpdateMethods();
        MissingPerson missingPerson = MissingPersonGenerator.generateUpdatedMissingPerson();
        MissingPerson modifiedMissingPerson = missingPersonService.updateDescription(missingPerson);
        assertMissingPerson(modifiedMissingPerson);
        Assert.assertEquals(missingPerson.getDescription(), modifiedMissingPerson.getDescription());
        Assert.assertNotEquals(missingPerson.getStatus(), modifiedMissingPerson.getStatus());
        Assert.assertNotEquals(missingPerson.getType(), modifiedMissingPerson.getType());
    }

    @Test
    public void testUpdateStatus() {
        mockUpdateMethods();
        MissingPerson missingPerson = MissingPersonGenerator.generateUpdatedMissingPerson();
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
    public void testExistsByIdAndType() {
        when(missingPersonRepository.findByIdAndType(Mockito.any(UUID.class), Mockito.any(MissingPersonType.class)))
                .thenReturn(Optional.of(new MissingPerson()));

        Assert.assertTrue(missingPersonService.existsByIdAndType(UUID.randomUUID(), MissingPersonType.SEEN));
    }

    @Test
    public void testDelete() {
        missingPersonService.delete(UUID.randomUUID());
        verify(missingPersonRepository, times(1)).deleteById(Mockito.any(UUID.class));

    }

    private void mockUpdateMethods() {
        when(missingPersonRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(MissingPersonGenerator.generateMissingPerson()));

        when(missingPersonRepository.save(Mockito.any(MissingPerson.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
    }

    private void assertMissingPerson(MissingPerson missingPerson) {
        Assert.assertNotNull(missingPerson);
        Assert.assertNotNull(missingPerson.getDescription());
        Assert.assertNotNull(missingPerson.getStatus());
        Assert.assertNotNull(missingPerson.getType());
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

    @Test(expected = DocumentNotFoundException.class)
    public void personShouldNotExits() {
        when(missingPersonRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        missingPersonService.findById(UUID.randomUUID());
    }
}
