package com.khaniv.openalert.api;

import com.khaniv.openalert.constants.MatchPaths;
import com.khaniv.openalert.dto.MatchDto;
import com.sun.istack.internal.NotNull;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient("open-alert-match")
@Service
@SuppressWarnings("unused")
public interface MatchApi {
    @GetMapping(MatchPaths.CONTROLLER + MatchPaths.ID)
    MatchDto findById(@PathVariable(value = "id") UUID id);

    @GetMapping(MatchPaths.CONTROLLER + MatchPaths.FIND_BY_LOST_PERSON_ID)
    List<MatchDto> findByLostPersonId(@PathVariable(value = "lostPersonId") UUID lostPersonId);

    @GetMapping(MatchPaths.CONTROLLER + MatchPaths.FIND_BY_SEEN_PERSON_ID)
    List<MatchDto> findBySeenPersonId(@PathVariable(value = "seenPersonId") UUID seenPersonId);

    @PostMapping(MatchPaths.CONTROLLER)
    MatchDto save(@RequestBody @NotNull MatchDto match);

    @PostMapping(MatchPaths.CONTROLLER + MatchPaths.SAVE_ALL)
    List<MatchDto> saveAll(@RequestBody @NotNull List<MatchDto> matches);

    @PutMapping(MatchPaths.CONTROLLER + MatchPaths.UPDATE_USER_STATUS)
    MatchDto updateUserStatus(@RequestBody @NotNull MatchDto match);

    @PutMapping(MatchPaths.CONTROLLER + MatchPaths.UPDATE_OPERATOR_STATUS)
    MatchDto updateOperatorStatus(@RequestBody @NotNull MatchDto match);

    @PutMapping(MatchPaths.CONTROLLER + MatchPaths.ACTIVATE)
    MatchDto activate(@PathVariable(value = "id") UUID id);

    @PutMapping(MatchPaths.CONTROLLER + MatchPaths.INACTIVATE)
    MatchDto inactivate(@PathVariable(value = "id") UUID id);

    @DeleteMapping(MatchPaths.CONTROLLER + MatchPaths.ID)
    void delete(@PathVariable(value = "id") UUID id);

    @PutMapping(MatchPaths.CONTROLLER + MatchPaths.VIEWED_BY_USER)
    MatchDto viewedByUser(@PathVariable(value = "id") UUID id);

    @PutMapping(MatchPaths.CONTROLLER + MatchPaths.VIEWED_BY_OPERATOR)
    MatchDto viewedByOperator(@PathVariable(value = "id") UUID id);
}
