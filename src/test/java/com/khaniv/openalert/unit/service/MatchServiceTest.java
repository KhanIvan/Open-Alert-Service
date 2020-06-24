package com.khaniv.openalert.unit.service;

import com.khaniv.openalert.documents.Match;
import com.khaniv.openalert.documents.enums.OperatorMatchStatus;
import com.khaniv.openalert.documents.enums.UserMatchStatus;
import com.khaniv.openalert.repositories.MatchRepository;
import com.khaniv.openalert.services.MatchService;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MatchServiceTest {
    private MatchService matchService;

    @Mock
    MissingPersonService missingPersonService;
    @Mock
    MatchRepository matchRepository;

    @Before
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
        matchService = new MatchService(matchRepository, missingPersonService);
    }

    @Test
    public void testSave() {
        when(matchRepository.existsByLostPersonIdAndSeenPersonId(Mockito.any(UUID.class), Mockito.any(UUID.class)))
                .thenReturn(false);

        when(missingPersonService.existsById(Mockito.any(UUID.class)))
                .thenReturn(true);

        when(matchRepository.save(Mockito.any(Match.class))).
                thenAnswer(AdditionalAnswers.returnsFirstArg());

        Match match = generateNewMatch();
        Match savedMatch = matchService.save(match);
        assertSavedMatch(match, savedMatch);
    }

    @Test
    public void testSaveAll() {
        when(matchRepository.existsByLostPersonIdAndSeenPersonId(Mockito.any(UUID.class), Mockito.any(UUID.class)))
                .thenReturn(false);

        when(missingPersonService.existsById(Mockito.any(UUID.class)))
                .thenReturn(true);

        when(matchRepository.saveAll(Mockito.anyCollection())).
                thenAnswer(AdditionalAnswers.returnsFirstArg());

        List<Match> matches = Stream.generate(this::generateNewMatch).limit(MatchService.MAX_COUNT).collect(Collectors.toList());
        List<Match> savedMatches = matchService.saveAll(matches);
        Assert.assertNotNull(savedMatches);
        Assert.assertEquals(matches.size(), savedMatches.size());
        assertSavedMatch(matches.get(0), savedMatches.get(0));
    }

    @Test
    public void testFindById() {
        when(matchRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(generateMatch()));

        Match match = matchService.findById(UUID.randomUUID());
        assertMatch(match);
    }

    @Test
    public void testFindByLostPersonId() {
        when(matchService.findByLostPersonId(Mockito.any(UUID.class)))
                .thenReturn(Collections.singletonList(generateMatch()));

        List<Match> matches = matchService.findByLostPersonId(UUID.randomUUID());
        Assert.assertNotNull(matches);
        Assert.assertEquals(1, matches.size());
        assertMatch(matches.get(0));
    }

    @Test
    public void testFindBySeenPersonId() {
        when(matchService.findBySeenPersonId(Mockito.any(UUID.class)))
                .thenReturn(Collections.singletonList(generateMatch()));

        List<Match> matches = matchService.findBySeenPersonId(UUID.randomUUID());
        Assert.assertNotNull(matches);
        Assert.assertEquals(1, matches.size());
        assertMatch(matches.get(0));
    }

    @Test
    public void testUpdateOperatorStatus() {
        mockUpdateMethods();
        Match match = generateModifiedStatusesMatch();
        Match modifiedMatch = matchService.updateOperatorStatus(match);
        assertMatch(modifiedMatch);
        Assert.assertEquals(match.getOperatorMatchStatus(), modifiedMatch.getOperatorMatchStatus());
        Assert.assertNotEquals(match.getUserMatchStatus(), modifiedMatch.getUserMatchStatus());
    }

    @Test
    public void testUpdateUserStatus() {
        mockUpdateMethods();
        Match match = generateModifiedStatusesMatch();
        Match modifiedMatch = matchService.updateUserStatus(match);
        assertMatch(modifiedMatch);
        Assert.assertNotEquals(match.getOperatorMatchStatus(), modifiedMatch.getOperatorMatchStatus());
        Assert.assertEquals(match.getUserMatchStatus(), modifiedMatch.getUserMatchStatus());
    }

    @Test
    public void testUpdateActive() {
        mockUpdateMethods();
        Match modifiedMatch = matchService.updateActive(UUID.randomUUID(), false);
        assertMatch(modifiedMatch);
        Assert.assertFalse(modifiedMatch.getActive());
    }

    @Test
    public void testUpdateViewedByUser() {
        mockUpdateMethods();
        Match modifiedMatch = matchService.viewedByUser(UUID.randomUUID());
        assertMatch(modifiedMatch);
        Assert.assertTrue(modifiedMatch.getViewedByUser());
    }

    @Test
    public void testUpdateViewedByOperator() {
        mockUpdateMethods();
        Match modifiedMatch = matchService.viewedByOperator(UUID.randomUUID());
        assertMatch(modifiedMatch);
        Assert.assertTrue(modifiedMatch.getViewedByOperator());
    }

    @Test
    public void testUpdateAllOperatorStatuses() {
        Match match = generateMatch();

        when(matchRepository.findAllById(Mockito.anyCollection()))
                .thenReturn(Collections.singletonList(match));

        when(matchRepository.saveAll(Mockito.anyCollection()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        Match matchToUpdate = generateModifiedStatusesMatch();
        matchToUpdate.setId(match.getId());
        List<Match> matches = Collections.singletonList(matchToUpdate);
        List<Match> modifiedMatches = matchService.updateAllOperatorStatuses(matches);
        assertSingletonListOfMatches(modifiedMatches);
        Assert.assertEquals(matches.get(0).getOperatorMatchStatus(), modifiedMatches.get(0).getOperatorMatchStatus());
        Assert.assertNotEquals(matches.get(0).getUserMatchStatus(), modifiedMatches.get(0).getUserMatchStatus());
    }

    @Test
    public void testUpdateAllUserStatuses() {
        Match match = generateMatch();

        when(matchRepository.findAllById(Mockito.anyCollection()))
                .thenReturn(Collections.singletonList(match));

        when(matchRepository.saveAll(Mockito.anyCollection()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        Match matchToUpdate = generateModifiedStatusesMatch();
        matchToUpdate.setId(match.getId());
        List<Match> matches = Collections.singletonList(matchToUpdate);
        List<Match> modifiedMatches = matchService.updateAllUserStatuses(matches);
        assertSingletonListOfMatches(modifiedMatches);
        Assert.assertNotEquals(matches.get(0).getOperatorMatchStatus(), modifiedMatches.get(0).getOperatorMatchStatus());
        Assert.assertEquals(matches.get(0).getUserMatchStatus(), modifiedMatches.get(0).getUserMatchStatus());
    }

    @Test
    public void testUpdateAllActive() {
        mockUpdateAllMethods();
        List<Match> matches = matchService.updateAllActive(Collections.singletonList(UUID.randomUUID()), false);
        assertSingletonListOfMatches(matches);
        Assert.assertFalse(matches.get(0).getActive());
    }

    @Test
    public void testUpdateAllViewedByUser() {
        mockUpdateAllMethods();
        List<Match> matches = matchService.updateAllViewedByUser(Collections.singletonList(UUID.randomUUID()));
        assertSingletonListOfMatches(matches);
        Assert.assertTrue(matches.get(0).getViewedByUser());
    }

    @Test
    public void testUpdateAllViewedByOperator() {
        mockUpdateAllMethods();
        List<Match> matches = matchService.updateAllViewedByOperator(Collections.singletonList(UUID.randomUUID()));
        assertSingletonListOfMatches(matches);
        Assert.assertTrue(matches.get(0).getViewedByOperator());
    }

    private void mockUpdateMethods() {
        when(matchRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(generateMatch()));

        when(matchRepository.save(Mockito.any(Match.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
    }

    private void mockUpdateAllMethods() {
        when(matchRepository.findAllById(Mockito.anyCollection()))
                .thenReturn(Collections.singletonList(generateMatch()));

        when(matchRepository.saveAll(Mockito.anyCollection()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
    }

    private void assertMatch(Match match) {
        Assert.assertNotNull(match);
        Assert.assertNotNull(match.getId());
        Assert.assertNotNull(match.getLostPersonId());
        Assert.assertNotNull(match.getSeenPersonId());
        Assert.assertNotNull(match.getOperatorMatchStatus());
        Assert.assertNotNull(match.getUserMatchStatus());
        Assert.assertNotNull(match.getActive());
        Assert.assertNotNull(match.getViewedByOperator());
        Assert.assertNotNull(match.getViewedByUser());
    }

    private void assertSavedMatch(Match matchToSave, Match savedMatch) {
        assertMatch(savedMatch);
        Assert.assertEquals(matchToSave.getSeenPersonId(), savedMatch.getSeenPersonId());
        Assert.assertEquals(matchToSave.getLostPersonId(), savedMatch.getLostPersonId());
        Assert.assertEquals(true, savedMatch.getActive());
        Assert.assertEquals(false, savedMatch.getViewedByOperator());
        Assert.assertEquals(false, savedMatch.getViewedByUser());
        Assert.assertEquals(UserMatchStatus.NOT_CONFIRMED, savedMatch.getUserMatchStatus());
        Assert.assertEquals(OperatorMatchStatus.ACTIVE, savedMatch.getOperatorMatchStatus());
    }

    private void assertSingletonListOfMatches(List<Match> matches) {
        Assert.assertNotNull(matches);
        Assert.assertEquals(1, matches.size());
        assertMatch(matches.get(0));
    }

    private Match generateNewMatch() {
        return Match.builder()
                .seenPersonId(UUID.randomUUID())
                .lostPersonId(UUID.randomUUID())
                .probability(0.5)
                .build();
    }

    private Match generateMatch() {
        return Match.builder()
                .id(UUID.randomUUID())
                .probability(0.5)
                .lostPersonId(UUID.randomUUID())
                .seenPersonId(UUID.randomUUID())
                .userMatchStatus(UserMatchStatus.NOT_CONFIRMED)
                .operatorMatchStatus(OperatorMatchStatus.ACTIVE)
                .active(true)
                .viewedByOperator(false)
                .viewedByUser(false)
                .build();
    }

    private Match generateModifiedStatusesMatch() {
        Match match = generateMatch();
        match.setUserMatchStatus(UserMatchStatus.CONFIRMED);
        match.setOperatorMatchStatus(OperatorMatchStatus.PROBABLE);
        return match;
    }


    @Test(expected = IllegalArgumentException.class)
    public void personShouldNotBeFound() {
        when(missingPersonService.existsById(Mockito.any(UUID.class)))
                .thenReturn(false);

        Match match = Match.builder()
                .lostPersonId(UUID.randomUUID())
                .seenPersonId(UUID.randomUUID())
                .build();

        matchService.save(match);
    }

    @Test(expected = IllegalArgumentException.class)
    public void matchShouldAlreadyExist() {
        when(matchRepository.existsByLostPersonIdAndSeenPersonId(Mockito.any(UUID.class), Mockito.any(UUID.class)))
                .thenReturn(true);

        when(missingPersonService.existsById(Mockito.any(UUID.class)))
                .thenReturn(true);

        Match match = Match.builder()
                .lostPersonId(UUID.randomUUID())
                .seenPersonId(UUID.randomUUID())
                .build();

        matchService.save(match);
    }

    @Test(expected = IllegalArgumentException.class)
    public void matchesSizeShouldBeTooBig() {
        List<Match> matches = Stream.generate(Match::new).limit(MatchService.MAX_COUNT + 1).collect(Collectors.toList());
        matchService.findAll(matches);
    }
}
