package com.khaniv.openalert.api;

import com.khaniv.openalert.constants.MissingPersonPaths;
import com.khaniv.openalert.dto.MissingPersonDto;
import com.sun.istack.internal.NotNull;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "open-alert-person")
@Service
@SuppressWarnings("unused")
public interface MissingPersonApi {
    @GetMapping(MissingPersonPaths.CONTROLLER + MissingPersonPaths.FIND_LOST)
    List<MissingPersonDto> findLostPersons();

    @GetMapping(MissingPersonPaths.CONTROLLER + MissingPersonPaths.FIND_SEEN)
    List<MissingPersonDto> findSeenPersons();

    @GetMapping(MissingPersonPaths.CONTROLLER + MissingPersonPaths.ID)
    MissingPersonDto findMissingPerson(@PathVariable(value = "id") UUID id);

    @PostMapping(MissingPersonPaths.CONTROLLER)
    MissingPersonDto save(@RequestBody @NotNull MissingPersonDto missingPerson);

    @PutMapping(MissingPersonPaths.CONTROLLER + MissingPersonPaths.UPDATE_DESCRIPTION)
    MissingPersonDto updateDescription(@RequestBody @NotNull MissingPersonDto source);

    @PutMapping(MissingPersonPaths.CONTROLLER + MissingPersonPaths.UPDATE_STATUS)
    MissingPersonDto updateStatus(@RequestBody @NotNull MissingPersonDto source);

    @DeleteMapping(MissingPersonPaths.CONTROLLER + MissingPersonPaths.ID)
    void delete(@PathVariable(value = "id") UUID id);

    @PutMapping(MissingPersonPaths.CONTROLLER + MissingPersonPaths.INACTIVATE)
    MissingPersonDto inactivate(@PathVariable(value = "id") UUID id);

    @PutMapping(MissingPersonPaths.CONTROLLER + MissingPersonPaths.ACTIVATE)
    MissingPersonDto activate(@PathVariable(value = "id") UUID id);
}
