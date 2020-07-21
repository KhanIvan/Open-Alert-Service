package com.khaniv.openalert.controllers;

import com.khaniv.openalert.documents.Match;
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
@RequestMapping("v1/match")
@Log4j2
@SuppressWarnings("unused")
public class MatchController {
    private final MatchService matchService;

    @GetMapping("/{id}")
    @ResponseBody
    public Match findById(@PathVariable(value = "id") UUID id) {
        log.info("Find match by ID {}", id);
        return matchService.findById(id);
    }

    @GetMapping("/lost/{lostPersonId}")
    @ResponseBody
    public List<Match> findByLostPersonId(@PathVariable(value = "lostPersonId") UUID lostPersonId) {
        log.info("Find matches by lost person ID {}", lostPersonId);
        return matchService.findByLostPersonId(lostPersonId);
    }

    @GetMapping("/seen/{seenPersonId}")
    @ResponseBody
    public List<Match> findBySeenPersonId(@PathVariable(value = "seenPersonId") UUID seenPersonId) {
        log.info("Find matches by seen person ID {}", seenPersonId);
        return matchService.findBySeenPersonId(seenPersonId);
    }

    @PostMapping
    @ResponseBody
    public Match save(@RequestBody @NotNull Match match) {
        log.info("Save match");
        return matchService.save(match);
    }

    @PostMapping("/all")
    @ResponseBody
    public List<Match> saveAll(@RequestBody @NotNull List<Match> matches) {
        log.info("Save all matches");
        return matchService.saveAll(matches);
    }

    @PutMapping("/status/user")
    @ResponseBody
    public Match updateUserStatus(@RequestBody @NotNull Match match) {
        log.info("Change user status to {} by ID {}", match.getUserMatchStatus(), match.getId());
        return matchService.updateUserStatus(match);
    }

    @PutMapping("/status/user/all")
    @ResponseBody
    public List<Match> updateAllUserStatuses(@RequestBody @NotNull List<Match> matches) {
        log.info("Change user statuses for matches: {}", getMatchesString(matches));
        return matchService.updateAllUserStatuses(matches);
    }

    @PutMapping("/status/operator")
    @ResponseBody
    public Match updateOperatorStatus(@RequestBody @NotNull Match match) {
        log.info("Change operator status to {} by ID {}", match.getOperatorMatchStatus(), match.getId());
        return matchService.updateOperatorStatus(match);
    }

    @PutMapping("/status/operator/all")
    @ResponseBody
    public List<Match> updateAllOperatorStatuses(@RequestBody @NotNull List<Match> matches) {
        log.info("Change operator statuses for matches: {}", getMatchesString(matches));
        return matchService.updateAllOperatorStatuses(matches);
    }

    @PutMapping("/activate/{id}")
    @ResponseBody
    public Match activate(@PathVariable(value = "id") UUID id) {
        log.info("Activate match by ID {}", id);
        return matchService.updateActive(id, true);
    }

    @PutMapping("/activate/all")
    @ResponseBody
    public List<Match> activateAll(@RequestBody List<UUID> ids) {
        log.info("Activate matches by IDs: {}", ids.stream().map(UUID::toString).collect(Collectors.joining(", ")));
        return matchService.updateAllActive(ids, true);
    }

    @PutMapping("/inactivate/{id}")
    @ResponseBody
    public Match inactivate(@PathVariable(value = "id") UUID id) {
        log.info("Inactivate match by ID {}", id);
        return matchService.updateActive(id, false);
    }

    @PutMapping("/inactivate/all")
    @ResponseBody
    public List<Match> inactivateAll(@RequestBody List<UUID> ids) {
        log.info("Inactivate matches by IDs: {}", getIdsString(ids));
        return matchService.updateAllActive(ids, false);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable(value = "id") UUID id) {
        log.info("Delete match by ID {}", id);
        matchService.delete(id);
    }

    @PutMapping("/viewed/user/{id}")
    @ResponseBody
    public Match viewedByUser(@PathVariable(value = "id") UUID id) {
        log.info("Match viewed by user by ID {}", id);
        return matchService.viewedByUser(id);
    }

    @PutMapping("/viewed/user/all")
    @ResponseBody
    public List<Match> viewedByUser(@RequestBody @NotNull List<UUID> ids) {
        log.info("Matches viewed by user by IDs: {}", getIdsString(ids));
        return matchService.updateAllViewedByUser(ids);
    }

    @PutMapping("/viewed/operator/{id}")
    @ResponseBody
    public Match viewedByOperator(@PathVariable(value = "id") UUID id) {
        log.info("Match viewed by operator by ID {}", id);
        return matchService.viewedByOperator(id);
    }

    @PutMapping("/viewed/operator/all")
    @ResponseBody
    public List<Match> viewedByOperator(@RequestBody @NotNull List<UUID> ids) {
        log.info("Matches viewed by operator by IDs: {}", getIdsString(ids));
        return matchService.updateAllViewedByOperator(ids);
    }

    private String getMatchesString(List<Match> matches) {
        return matches.stream().map(Match::getId).map(UUID::toString).collect(Collectors.joining(", "));
    }

    private String getIdsString(List<UUID> ids) {
        return ids.stream().map(UUID::toString).collect(Collectors.joining(", "));
    }
}
