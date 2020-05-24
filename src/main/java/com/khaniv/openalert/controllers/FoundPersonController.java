package com.khaniv.openalert.controllers;

import com.khaniv.openalert.documents.FoundPerson;
import com.khaniv.openalert.documents.PersonDescription;
import com.khaniv.openalert.documents.enums.PersonSex;
import com.khaniv.openalert.services.FoundPersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/found")
@Log4j2
public class FoundPersonController {
    @Autowired
    private FoundPersonService foundPersonService;

    @GetMapping
    public List<FoundPerson> findAll() {
        log.debug("Find all found persons request");
        return foundPersonService.findAll();
    }

    @GetMapping("/{id}")
    public FoundPerson findById(@PathVariable(value = "id") UUID id) {
        log.debug("Find a found person by ID {} request", id);
        return foundPersonService.findById(id);
    }

    @PutMapping
    public FoundPerson save(@RequestBody FoundPerson foundPerson) {
        log.debug("Save a found person request");
        return foundPersonService.save(foundPerson);
    }

    @DeleteMapping
    public void deleteAll() {
        log.debug("Delete all found persons request");
        foundPersonService.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") UUID id) {
        log.debug("Delete a found person by ID {}", id);
        foundPersonService.delete(id);
    }
}
