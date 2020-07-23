package com.khaniv.openalert.tests.unit;

import com.khaniv.openalert.dto.MatchDto;
import com.khaniv.openalert.checkers.CheckingUtils;
import com.khaniv.openalert.documents.Match;
import com.khaniv.openalert.dto.enums.MissingPersonType;
import com.khaniv.openalert.dto.enums.OperatorMatchStatus;
import com.khaniv.openalert.dto.enums.UserMatchStatus;
import com.khaniv.openalert.errors.exceptions.DocumentDuplicatesException;
import com.khaniv.openalert.errors.exceptions.DocumentNotFoundException;
import com.khaniv.openalert.errors.exceptions.MatchAlreadyExistsException;
import com.khaniv.openalert.errors.exceptions.MaxCountExcessException;
import com.khaniv.openalert.helpers.generators.MatchGenerator;
import com.khaniv.openalert.mappers.MatchMapper;
import com.khaniv.openalert.repositories.MatchRepository;
import com.khaniv.openalert.services.MatchService;
import com.khaniv.openalert.services.MissingPersonService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.*;

/**
 * Unit test for {@link MatchService}
 *
 * @author Ivan Khan
 */

@RunWith(MockitoJUnitRunner.class)
public class MatchServiceTest {
    private MatchService matchService;
    private final MatchMapper matchMapper = Mappers.getMapper(MatchMapper.class);

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
        when(matchRepository.findByLostPersonIdAndSeenPersonId(Mockito.any(UUID.class), Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        when(missingPersonService.existsById(Mockito.any(UUID.class)))
                .thenReturn(true);

        when(matchRepository.save(Mockito.any(Match.class))).
                thenAnswer(AdditionalAnswers.returnsFirstArg());

        when(missingPersonService.existsByIdAndType(Mockito.any(UUID.class), Mockito.any(MissingPersonType.class)))
                .thenReturn(true);

        MatchDto match = matchMapper.toDto(MatchGenerator.generateNewMatch());
        MatchDto savedMatch = matchService.save(match);
        assertSavedMatch(match, savedMatch);
    }

    @Test
    public void testSaveAll() {
        when(matchRepository.findByLostPersonIdAndSeenPersonId(Mockito.any(UUID.class), Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        when(missingPersonService.existsById(Mockito.any(UUID.class)))
                .thenReturn(true);

        when(missingPersonService.existsByIdAndType(Mockito.any(UUID.class), Mockito.any(MissingPersonType.class)))
                .thenReturn(true);

        when(matchRepository.saveAll(Mockito.anyCollection())).
                thenAnswer(AdditionalAnswers.returnsFirstArg());

        List<MatchDto> matches = matchMapper.toDto(Arrays.asList(MatchGenerator.generateNewMatch(), MatchGenerator.generateNewMatch()));
        List<MatchDto> savedMatches = matchService.saveAll(matches);
        Assert.assertNotNull(savedMatches);
        Assert.assertEquals(matches.size(), savedMatches.size());
        assertSavedMatch(matches.get(0), savedMatches.get(0));
    }

    @Test
    public void testFindById() {
        when(matchRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(MatchGenerator.generateMatch()));

        MatchDto match = matchService.findById(UUID.randomUUID());
        assertMatch(match);
    }

    @Test
    public void testFindByLostPersonId() {
        when(matchRepository.findByLostPersonId(Mockito.any(UUID.class)))
                .thenReturn(Collections.singletonList(MatchGenerator.generateMatch()));

        List<MatchDto> matches = matchService.findByLostPersonId(UUID.randomUUID());
        Assert.assertNotNull(matches);
        Assert.assertEquals(1, matches.size());
        assertMatch(matches.get(0));
    }

    @Test
    public void testFindBySeenPersonId() {
        when(matchRepository.findBySeenPersonId(Mockito.any(UUID.class)))
                .thenReturn(Collections.singletonList(MatchGenerator.generateMatch()));

        List<MatchDto> matches = matchService.findBySeenPersonId(UUID.randomUUID());
        Assert.assertNotNull(matches);
        Assert.assertEquals(1, matches.size());
        assertMatch(matches.get(0));
    }

    @Test
    public void testUpdateOperatorStatus() {
        mockUpdateMethods();
        MatchDto match = matchMapper.toDto(MatchGenerator.generateModifiedStatusesMatch());
        MatchDto modifiedMatch = matchService.updateOperatorStatus(match);
        assertMatch(modifiedMatch);
        Assert.assertEquals(match.getOperatorMatchStatus(), modifiedMatch.getOperatorMatchStatus());
        Assert.assertNotEquals(match.getUserMatchStatus(), modifiedMatch.getUserMatchStatus());
    }

    @Test
    public void testUpdateUserStatus() {
        mockUpdateMethods();
        MatchDto match = matchMapper.toDto(MatchGenerator.generateModifiedStatusesMatch());
        MatchDto modifiedMatch = matchService.updateUserStatus(match);
        assertMatch(modifiedMatch);
        Assert.assertNotEquals(match.getOperatorMatchStatus(), modifiedMatch.getOperatorMatchStatus());
        Assert.assertEquals(match.getUserMatchStatus(), modifiedMatch.getUserMatchStatus());
    }

    @Test
    public void testUpdateActive() {
        mockUpdateMethods();
        MatchDto modifiedMatch = matchService.updateActive(UUID.randomUUID(), false);
        assertMatch(modifiedMatch);
        Assert.assertFalse(modifiedMatch.getActive());
    }

    @Test
    public void testUpdateViewedByUser() {
        mockUpdateMethods();
        MatchDto modifiedMatch = matchService.viewedByUser(UUID.randomUUID());
        assertMatch(modifiedMatch);
        Assert.assertTrue(modifiedMatch.getViewedByUser());
    }

    @Test
    public void testUpdateViewedByOperator() {
        mockUpdateMethods();
        MatchDto modifiedMatch = matchService.viewedByOperator(UUID.randomUUID());
        assertMatch(modifiedMatch);
        Assert.assertTrue(modifiedMatch.getViewedByOperator());
    }

    @Test
    public void testDelete() {
        matchService.delete(UUID.randomUUID());
        verify(matchRepository, times(1)).deleteById(Mockito.any(UUID.class));
    }

    private void mockUpdateMethods() {
        when(matchRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(MatchGenerator.generateMatch()));

        when(matchRepository.save(Mockito.any(Match.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
    }

    private void mockUpdateAllMethods() {
        when(matchRepository.findAllById(Mockito.anyCollection()))
                .thenReturn(Collections.singletonList(MatchGenerator.generateMatch()));

        when(matchRepository.saveAll(Mockito.anyCollection()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
    }

    private void assertMatch(MatchDto match) {
        Assert.assertNotNull(match);
        Assert.assertNotNull(match.getLostPersonId());
        Assert.assertNotNull(match.getSeenPersonId());
        Assert.assertNotNull(match.getOperatorMatchStatus());
        Assert.assertNotNull(match.getUserMatchStatus());
        Assert.assertNotNull(match.getViewedByOperator());
        Assert.assertNotNull(match.getViewedByUser());
    }

    private void assertSavedMatch(MatchDto matchToSave, MatchDto savedMatch) {
        assertMatch(savedMatch);
        Assert.assertEquals(matchToSave.getSeenPersonId(), savedMatch.getSeenPersonId());
        Assert.assertEquals(matchToSave.getLostPersonId(), savedMatch.getLostPersonId());
        Assert.assertEquals(false, savedMatch.getViewedByOperator());
        Assert.assertEquals(false, savedMatch.getViewedByUser());
        Assert.assertEquals(UserMatchStatus.NOT_CONFIRMED, savedMatch.getUserMatchStatus());
        Assert.assertEquals(OperatorMatchStatus.ACTIVE, savedMatch.getOperatorMatchStatus());
    }

    private void assertSingletonListOfMatches(List<MatchDto> matches) {
        Assert.assertNotNull(matches);
        Assert.assertEquals(1, matches.size());
        assertMatch(matches.get(0));
    }


    @Test(expected = DocumentNotFoundException.class)
    public void personShouldNotBeFound() {
        when(missingPersonService.existsById(Mockito.any(UUID.class)))
                .thenReturn(false);

        MatchDto match = matchMapper.toDto(MatchGenerator.generateNewMatch());
        matchService.save(match);
    }

    @Test(expected = MatchAlreadyExistsException.class)
    public void matchShouldAlreadyExist() {
        when(matchRepository.findByLostPersonIdAndSeenPersonId(Mockito.any(UUID.class), Mockito.any(UUID.class)))
                .thenReturn(Optional.of(new Match()));

        when(missingPersonService.existsById(Mockito.any(UUID.class)))
                .thenReturn(true);

        when(missingPersonService.existsByIdAndType(Mockito.any(UUID.class), Mockito.any(MissingPersonType.class)))
                .thenReturn(true);

        MatchDto match = matchMapper.toDto(MatchGenerator.generateNewMatch());
        matchService.save(match);
    }

    @Test(expected = MaxCountExcessException.class)
    public void matchesSizeShouldBeTooBig() {
        List<MatchDto> matches = Collections.nCopies(CheckingUtils.MAX_COUNT + 1, matchMapper.toDto(MatchGenerator.generateMatch()));
        matchService.findAll(matches);
    }

    @Test(expected = DocumentNotFoundException.class)
    public void personShouldNotExistByIdAndType() {
        when(missingPersonService.existsById(Mockito.any(UUID.class)))
                .thenReturn(true);

        when(missingPersonService.existsByIdAndType(Mockito.any(UUID.class), Mockito.any(MissingPersonType.class)))
                .thenReturn(false);

        Match match = MatchGenerator.generateNewMatch();
        matchService.save(matchMapper.toDto(match));
    }

    @Test(expected = DocumentDuplicatesException.class)
    public void matchesShouldBeDuplicates() {
        when(matchRepository.findByLostPersonIdAndSeenPersonId(Mockito.any(UUID.class), Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        when(missingPersonService.existsById(Mockito.any(UUID.class)))
                .thenReturn(true);

        when(missingPersonService.existsByIdAndType(Mockito.any(UUID.class), Mockito.any(MissingPersonType.class)))
                .thenReturn(true);

        UUID lostPersonId = UUID.randomUUID();
        UUID seenPersonId = UUID.randomUUID();
        List<Match> matches = Arrays.asList(
                Match.builder()
                        .probability(0.0)
                        .seenPersonId(seenPersonId)
                        .lostPersonId(lostPersonId)
                        .build(),
                Match.builder()
                        .probability(0.5)
                        .seenPersonId(seenPersonId)
                        .lostPersonId(lostPersonId)
                        .build()
        );

        matchService.saveAll(matchMapper.toDto(matches));
    }
}
