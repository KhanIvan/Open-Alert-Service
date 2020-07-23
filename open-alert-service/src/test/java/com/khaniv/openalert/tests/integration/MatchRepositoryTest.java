package com.khaniv.openalert.tests.integration;


import com.khaniv.openalert.configurations.MongoConfiguration;
import com.khaniv.openalert.documents.Match;
import com.khaniv.openalert.helpers.generators.MatchGenerator;
import com.khaniv.openalert.helpers.utils.AssertionUtils;
import com.khaniv.openalert.listeners.BaseDocumentEventListener;
import com.khaniv.openalert.repositories.MatchRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Integration test for {@link MatchRepository}
 *
 * @author Ivan Khan
 */

@Import({MongoConfiguration.class, BaseDocumentEventListener.class})
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class MatchRepositoryTest {
    @Autowired
    private MatchRepository matchRepository;

    @AfterEach
    public void clear() {
        matchRepository.deleteAll();
    }

    @Test
    public void testSave() {
        Match match = MatchGenerator.generateMatch();
        matchRepository.save(match);
        List<Match> matches = matchRepository.findAll();
        AssertionUtils.assertSingletonCollection(match, matches);
        AssertionUtils.assertNewBaseDocument(matches.get(0));
    }

    @Test
    public void testUpdate() throws InterruptedException {
        Match match = MatchGenerator.generateMatch();
        matchRepository.save(match);
        Thread.sleep(1);
        matchRepository.save(match);
        List<Match> matches = matchRepository.findAll();
        AssertionUtils.assertSingletonCollection(match, matches);
        AssertionUtils.assertUpdatedBaseDocument(matches.get(0));
    }

    @Test
    public void testExistsByLostPersonIdAndSeenPersonId() {
        Match match = MatchGenerator.generateMatch();
        matchRepository.save(match);
        Optional<Match> result = matchRepository.findByLostPersonIdAndSeenPersonId(match.getLostPersonId(), match.getSeenPersonId());
        Assert.assertTrue(result.isPresent());
        result = matchRepository.findByLostPersonIdAndSeenPersonId(match.getLostPersonId(), UUID.randomUUID());
        Assert.assertFalse(result.isPresent());
        result = matchRepository.findByLostPersonIdAndSeenPersonId(UUID.randomUUID(), match.getSeenPersonId());
        Assert.assertFalse(result.isPresent());
        result = matchRepository.findByLostPersonIdAndSeenPersonId(UUID.randomUUID(), UUID.randomUUID());
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testFindByLostPersonId() {
        Match match = MatchGenerator.generateMatch();
        matchRepository.save(match);
        List<Match> matches = matchRepository.findByLostPersonId(match.getLostPersonId());
        AssertionUtils.assertSingletonCollection(match, matches);
        matches = matchRepository.findByLostPersonId(UUID.randomUUID());
        AssertionUtils.assertEmptyCollection(matches);
    }

    @Test
    public void testFindBySeenPersonId() {
        Match match = MatchGenerator.generateMatch();
        matchRepository.save(match);
        List<Match> matches = matchRepository.findBySeenPersonId(match.getSeenPersonId());
        AssertionUtils.assertSingletonCollection(match, matches);
        matches = matchRepository.findBySeenPersonId(UUID.randomUUID());
        AssertionUtils.assertEmptyCollection(matches);
    }
}
