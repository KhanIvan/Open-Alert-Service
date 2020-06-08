package com.khaniv.openalert.controllers;

import com.khaniv.openalert.documents.Match;
import com.khaniv.openalert.services.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/match")
@Log4j2
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
    public Match save(@RequestBody Match match) {
        log.info("Save match");
        return matchService.save(match);
    }

    @PutMapping("/status/user")
    @ResponseBody
    public Match updateUserStatus(@RequestBody Match match) {
        log.info("Change user status to {} by ID {}", match.getUserMatchStatus(), match.getId());
        return matchService.updateUserStatus(match);
    }

    @PutMapping("/status/operator")
    @ResponseBody
    public Match updateOperatorStatus(@RequestBody Match match) {
        log.info("Change operator status to {} by ID {}", match.getOperatorMatchStatus(), match.getId());
        return matchService.updateOperatorStatus(match);
    }

    @PutMapping("/activate/{id}")
    @ResponseBody
    public Match activate(@PathVariable(value = "id") UUID id) {
        log.info("Activate match by ID {}", id);
        return matchService.updateActive(id, true);
    }

    @PutMapping("/inactivate/{id}")
    @ResponseBody
    public Match inactivate(@PathVariable(value = "id") UUID id) {
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
    public Match viewedByUser(@PathVariable(value = "id") UUID id) {
        log.info("Match viewed by user by ID {}", id);
        return matchService.viewedByUser(id);
    }

    @PutMapping("/viewed/operator/{id}")
    @ResponseBody
    public Match viewedByOperator(@PathVariable(value = "id") UUID id) {
        log.info("Match viewed by operator by ID {}", id);
        return matchService.viewedByOperator(id);
    }
}
