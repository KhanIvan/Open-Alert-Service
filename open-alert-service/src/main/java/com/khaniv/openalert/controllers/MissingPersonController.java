package com.khaniv.openalert.controllers;

import com.khaniv.openalert.constants.MissingPersonPaths;
import com.khaniv.openalert.dto.MissingPersonDto;
import com.khaniv.openalert.dto.enums.MissingPersonType;
import com.khaniv.openalert.services.MissingPersonService;
import com.sun.istack.internal.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(MissingPersonPaths.CONTROLLER)
@Log4j2
@SuppressWarnings("unused")
public class MissingPersonController {
    private final MissingPersonService missingPersonService;

    @GetMapping(MissingPersonPaths.FIND_LOST)
    @ResponseBody
    public List<MissingPersonDto> findLostPersons() {
        log.info("Find all lost persons");
        return missingPersonService.findByTypeAndActive(MissingPersonType.LOST, true);
    }

    @GetMapping(MissingPersonPaths.FIND_SEEN)
    @ResponseBody
    public List<MissingPersonDto> findSeenPersons() {
        log.info("Find all seen person");
        return missingPersonService.findByTypeAndActive(MissingPersonType.SEEN, true);
    }

    @GetMapping(MissingPersonPaths.ID)
    @ResponseBody
    public MissingPersonDto findMissingPerson(@PathVariable(value = "id") UUID id) {
        log.info("Find lost person by ID: {}", id);
        return missingPersonService.findById(id);
    }

    @PostMapping
    @ResponseBody
    public MissingPersonDto save(@RequestBody @NotNull MissingPersonDto missingPerson) {
        log.info("Save missing person");
        return missingPersonService.save(missingPerson);
    }

    @PutMapping(MissingPersonPaths.UPDATE_DESCRIPTION)
    @ResponseBody
    public MissingPersonDto updateDescription(@RequestBody @NotNull MissingPersonDto source) {
        log.info("Update description of a missing person by ID {}", source.getId());
        return missingPersonService.updateDescription(source);
    }

    @PutMapping(MissingPersonPaths.UPDATE_STATUS)
    @ResponseBody
    public MissingPersonDto updateStatus(@RequestBody @NotNull MissingPersonDto source) {
        log.info("Update status of a missing person by ID: {}", source.getId());
        return missingPersonService.updateStatus(source);
    }

    @DeleteMapping(MissingPersonPaths.ID)
    @ResponseBody
    public void delete(@PathVariable(value = "id") UUID id) {
        log.info("Delete a missing person by ID {}", id);
        missingPersonService.delete(id);
    }

    @PutMapping(MissingPersonPaths.INACTIVATE)
    @ResponseBody
    public MissingPersonDto inactivate(@PathVariable(value = "id") UUID id) {
        log.info("Inactivate a missing person by ID: {}", id);
        return missingPersonService.updateActive(id, false);
    }

    @PutMapping(MissingPersonPaths.ACTIVATE)
    @ResponseBody
    public MissingPersonDto activate(@PathVariable(value = "id") UUID id) {
        log.info("Activate a missing person by ID: {}", id);
        return missingPersonService.updateActive(id, true);
    }
}
