package com.khaniv.openalert.controllers;

import com.khaniv.openalert.documents.LostPerson;
import com.khaniv.openalert.services.LostPersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/lost")
@RequiredArgsConstructor
@Log4j2
public class LostPersonController {
    @Autowired
    private LostPersonService lostPersonService;

    @GetMapping
    public List<LostPerson> findAll() {
        log.debug("Find all lost persons request");
        return lostPersonService.findAll();
    }

    @GetMapping("/{id}")
    public LostPerson findById(@PathVariable(value = "id") UUID id) {
        log.debug("Find a lost person by ID {} request", id);
        return lostPersonService.findById(id);
    }

    @PutMapping
    public LostPerson save(@RequestBody LostPerson lostPerson) {
        log.debug("Save a lost person request");
        return lostPersonService.save(lostPerson);
    }

    @DeleteMapping
    public void deleteAll() {
        log.debug("Delete all lost persons request");
        lostPersonService.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") UUID id) {
        log.debug("Delete a lost person by ID {}", id);
        lostPersonService.delete(id);
    }

}
