package com.khaniv.openalert.integration;

import com.khaniv.openalert.configurations.MongoConfiguration;
import com.khaniv.openalert.documents.MissingPerson;
import com.khaniv.openalert.documents.enums.MissingPersonType;
import com.khaniv.openalert.generators.MissingPersonGenerator;
import com.khaniv.openalert.listeners.BaseDocumentEventListener;
import com.khaniv.openalert.repositories.MissingPersonRepository;
import com.khaniv.openalert.utils.AssertionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * Integration test for {@link MissingPersonRepository}
 *
 * @author Ivan Khan
 */

@Import({MongoConfiguration.class, BaseDocumentEventListener.class})
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class MissingPersonRepositoryTest {
    @Autowired
    private MissingPersonRepository missingPersonRepository;

    @AfterEach
    public void clear() {
        missingPersonRepository.deleteAll();
    }

    @Test
    public void testSave() {
        MissingPerson missingPerson = MissingPersonGenerator.generateMissingPerson();
        missingPersonRepository.save(missingPerson);
        List<MissingPerson> missingPeople = missingPersonRepository.findAll();
        AssertionUtils.assertSingletonCollection(missingPerson, missingPeople);
        AssertionUtils.assertNewBaseDocument(missingPeople.get(0));
    }

    @Test
    public void testUpdate() throws InterruptedException {
        MissingPerson missingPerson = MissingPersonGenerator.generateMissingPerson();
        missingPersonRepository.save(missingPerson);
        Thread.sleep(1);
        missingPersonRepository.save(missingPerson);
        List<MissingPerson> missingPeople = missingPersonRepository.findAll();
        AssertionUtils.assertSingletonCollection(missingPerson, missingPeople);
        AssertionUtils.assertUpdatedBaseDocument(missingPeople.get(0));
    }

    @Test
    public void testFindByTypeAndActive() {
        MissingPerson missingPerson = MissingPersonGenerator.generateMissingPerson();
        missingPersonRepository.save(missingPerson);
        List<MissingPerson> missingPeople = missingPersonRepository.findByTypeAndActive(missingPerson.getType(), missingPerson.getActive());
        AssertionUtils.assertSingletonCollection(missingPerson, missingPeople);
        missingPeople = missingPersonRepository.findByTypeAndActive(MissingPersonType.SEEN, !missingPerson.getActive());
        AssertionUtils.assertEmptyCollection(missingPeople);
        missingPeople = missingPersonRepository.findByTypeAndActive(missingPerson.getType(), !missingPerson.getActive());
        AssertionUtils.assertEmptyCollection(missingPeople);
        missingPeople = missingPersonRepository.findByTypeAndActive(MissingPersonType.SEEN, missingPerson.getActive());
        AssertionUtils.assertEmptyCollection(missingPeople);
    }
}
