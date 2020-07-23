package com.khaniv.openalert.controllers;

import com.khaniv.openalert.MatchDto;
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
    public MatchDto findById(@PathVariable(value = "id") UUID id) {
        log.info("Find match by ID {}", id);
        return matchService.findById(id);
    }

    @GetMapping("/lost/{lostPersonId}")
    @ResponseBody
    public List<MatchDto> findByLostPersonId(@PathVariable(value = "lostPersonId") UUID lostPersonId) {
        log.info("Find matches by lost person ID {}", lostPersonId);
        return matchService.findByLostPersonId(lostPersonId);
    }

    @GetMapping("/seen/{seenPersonId}")
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

    @PostMapping("/all")
    @ResponseBody
    public List<MatchDto> saveAll(@RequestBody @NotNull List<MatchDto> matches) {
        log.info("Save all matches");
        return matchService.saveAll(matches);
    }

    @PutMapping("/status/user")
    @ResponseBody
    public MatchDto updateUserStatus(@RequestBody @NotNull MatchDto match) {
        log.info("Change user status to {} by ID {}", match.getUserMatchStatus(), match.getId());
        return matchService.updateUserStatus(match);
    }

    @PutMapping("/status/operator")
    @ResponseBody
    public MatchDto updateOperatorStatus(@RequestBody @NotNull MatchDto match) {
        log.info("Change operator status to {} by ID {}", match.getOperatorMatchStatus(), match.getId());
        return matchService.updateOperatorStatus(match);
    }

    @PutMapping("/activate/{id}")
    @ResponseBody
    public MatchDto activate(@PathVariable(value = "id") UUID id) {
        log.info("Activate match by ID {}", id);
        return matchService.updateActive(id, true);
    }


    @PutMapping("/inactivate/{id}")
    @ResponseBody
    public MatchDto inactivate(@PathVariable(value = "id") UUID id) {
        log.info("Inactivate match by ID {}", id);
        return matchService.updateActive(id, false);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable(value = "id") UUID id) {
        log.info("Delete match by ID {}", id);
        matchService.delete(id);
    }

    @PutMapping("/viewed/user/{id}")
    @ResponseBody
    public MatchDto viewedByUser(@PathVariable(value = "id") UUID id) {
        log.info("Match viewed by user by ID {}", id);
        return matchService.viewedByUser(id);
    }

    @PutMapping("/viewed/operator/{id}")
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
