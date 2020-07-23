package com.khaniv.openalert.controllers;

import com.khaniv.openalert.constants.MatchPaths;
import com.khaniv.openalert.dto.MatchDto;
import com.khaniv.openalert.services.MatchService;
import com.sun.istack.internal.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(MatchPaths.CONTROLLER)
@Log4j2
@SuppressWarnings("unused")
public class MatchController {
    private final MatchService matchService;

    @GetMapping(MatchPaths.ID)
    @ResponseBody
    public MatchDto findById(@PathVariable(value = "id") UUID id) {
        log.info("Find match by ID {}", id);
        return matchService.findById(id);
    }

    @GetMapping(MatchPaths.FIND_BY_LOST_PERSON_ID)
    @ResponseBody
    public List<MatchDto> findByLostPersonId(@PathVariable(value = "lostPersonId") UUID lostPersonId) {
        log.info("Find matches by lost person ID {}", lostPersonId);
        return matchService.findByLostPersonId(lostPersonId);
    }

    @GetMapping(MatchPaths.FIND_BY_SEEN_PERSON_ID)
    @ResponseBody
    public List<MatchDto> findBySeenPersonId(@PathVariable(value = "seenPersonId") UUID seenPersonId) {
        log.info("Find matches by seen person ID {}", seenPersonId);
        return matchService.findBySeenPersonId(seenPersonId);
    }

    @PostMapping
    @ResponseBody
    public MatchDto save(@RequestBody @NotNull MatchDto match) {
        log.info("Save match");
        return matchService.save(match);
    }

    @PostMapping(MatchPaths.SAVE_ALL)
    @ResponseBody
    public List<MatchDto> saveAll(@RequestBody @NotNull List<MatchDto> matches) {
        log.info("Save all matches");
        return matchService.saveAll(matches);
    }

    @PutMapping(MatchPaths.UPDATE_USER_STATUS)
    @ResponseBody
    public MatchDto updateUserStatus(@RequestBody @NotNull MatchDto match) {
        log.info("Change user status to {} by ID {}", match.getUserMatchStatus(), match.getId());
        return matchService.updateUserStatus(match);
    }

    @PutMapping(MatchPaths.UPDATE_OPERATOR_STATUS)
    @ResponseBody
    public MatchDto updateOperatorStatus(@RequestBody @NotNull MatchDto match) {
        log.info("Change operator status to {} by ID {}", match.getOperatorMatchStatus(), match.getId());
        return matchService.updateOperatorStatus(match);
    }

    @PutMapping(MatchPaths.ACTIVATE)
    @ResponseBody
    public MatchDto activate(@PathVariable(value = "id") UUID id) {
        log.info("Activate match by ID {}", id);
        return matchService.updateActive(id, true);
    }

    @PutMapping(MatchPaths.INACTIVATE)
    @ResponseBody
    public MatchDto inactivate(@PathVariable(value = "id") UUID id) {
        log.info("Inactivate match by ID {}", id);
        return matchService.updateActive(id, false);
    }

    @DeleteMapping(MatchPaths.ID)
    @ResponseBody
    public void delete(@PathVariable(value = "id") UUID id) {
        log.info("Delete match by ID {}", id);
        matchService.delete(id);
    }

    @PutMapping(MatchPaths.VIEWED_BY_USER)
    @ResponseBody
    public MatchDto viewedByUser(@PathVariable(value = "id") UUID id) {
        log.info("Match viewed by user by ID {}", id);
        return matchService.viewedByUser(id);
    }

    @PutMapping(MatchPaths.VIEWED_BY_OPERATOR)
    @ResponseBody
    public MatchDto viewedByOperator(@PathVariable(value = "id") UUID id) {
        log.info("Match viewed by operator by ID {}", id);
        return matchService.viewedByOperator(id);
    }

    private String getMatchesString(List<MatchDto> matches) {
        return matches.stream().map(MatchDto::getId).map(UUID::toString).collect(Collectors.joining(", "));
    }

    private String getIdsString(List<UUID> ids) {
        return ids.stream().map(UUID::toString).collect(Collectors.joining(", "));
    }
}
