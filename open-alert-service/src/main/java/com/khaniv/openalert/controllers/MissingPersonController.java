package com.khaniv.openalert.controllers;

import com.khaniv.openalert.documents.MissingPerson;
import com.khaniv.openalert.documents.enums.MissingPersonType;
import com.khaniv.openalert.services.MissingPersonService;
import com.sun.istack.internal.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/person")
@Log4j2
@SuppressWarnings("unused")
public class MissingPersonController {
    private final MissingPersonService missingPersonService;

    @GetMapping("/lost")
    @ResponseBody
    public List<MissingPerson> findLostPersons() {
        log.info("Find all lost persons");
        return missingPersonService.findByTypeAndActive(MissingPersonType.LOST, true);
    }

    @GetMapping("/seen")
    @ResponseBody
    public List<MissingPerson> findSeenPersons() {
        log.info("Find all seen person");
        return missingPersonService.findByTypeAndActive(MissingPersonType.SEEN, true);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public MissingPerson findMissingPerson(@PathVariable(value = "id") UUID id) {
        log.info("Find lost person by ID: {}", id);
        return missingPersonService.findById(id);
    }

    @PostMapping
    @ResponseBody
    public MissingPerson save(@RequestBody @NotNull MissingPerson missingPerson) {
        log.info("Save missing person");
        return missingPersonService.save(missingPerson);
    }

    @PutMapping("/description")
    @ResponseBody
    public MissingPerson updateDescription(@RequestBody @NotNull MissingPerson source) {
        log.info("Update description of a missing person by ID {}", source.getId());
        return missingPersonService.updateDescription(source);
    }

    @PutMapping("/status")
    @ResponseBody
    public MissingPerson updateStatus(@RequestBody @NotNull MissingPerson source) {
        log.info("Update status of a missing person by ID: {}", source.getId());
        return missingPersonService.updateStatus(source);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable(value = "id") UUID id) {
        log.info("Delete a missing person by ID {}", id);
        missingPersonService.delete(id);
    }

    @PutMapping("/inactivate/{id}")
    @ResponseBody
    public MissingPerson inactivate(@PathVariable(value = "id") UUID id) {
        log.info("Inactivate a missing person by ID: {}", id);
        return missingPersonService.updateActive(id, false);
    }

    @PutMapping("/activate/{id}")
    @ResponseBody
    public MissingPerson activate(@PathVariable(value = "id") UUID id) {
        log.info("Activate a missing person by ID: {}", id);
        return missingPersonService.updateActive(id, true);
    }
}
