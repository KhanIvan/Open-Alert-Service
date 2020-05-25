package com.khaniv.openalert.controllers;

import com.khaniv.openalert.documents.Person;
import com.khaniv.openalert.enums.PersonType;
import com.khaniv.openalert.enums.SearchStatus;
import com.khaniv.openalert.services.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/person")
@Log4j2
public class PersonController {
    private final PersonService personService;

    @GetMapping("/all")
    public ResponseEntity<List<Person>> findAll() {
        log.debug("Find all persons");
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable(value = "id") UUID id) {
        log.debug("Find a person by ID {}", id);
        return ResponseEntity.ok(personService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Person>> findByTypeAndActive(@RequestParam(value = "type") PersonType type,
                                                            @RequestParam(value = "active") Boolean active) {
        log.debug("Find persons by type {} and active {}", type, active);
        return ResponseEntity.ok(personService.findByTypeAndActive(type, active));
    }

    @PutMapping
    public ResponseEntity<Person> save(@RequestBody Person person) {
        log.debug("Save a person");
        return ResponseEntity.ok(personService.save(person));
    }

    @DeleteMapping
    public void deleteAll() {
        log.debug("Delete all persons");
        personService.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") UUID id) {
        log.debug("Delete a person by ID {}", id);
        personService.delete(id);
    }

    @PutMapping("/active")
    public ResponseEntity<Person> activate(@RequestParam(value = "id") UUID id,
                                           @RequestParam(value = "active") Boolean active) {
        log.debug("Change active to {} for person by ID {}", active, id);
        return ResponseEntity.ok(personService.updateActive(id, active));
    }

    @PutMapping("/status")
    public ResponseEntity<Person> updateStatus(@RequestParam(value = "id") UUID id,
                                               @RequestParam(value = "status") SearchStatus status) {
        log.debug("Change status to {} for person {}", status, id);
        return ResponseEntity.ok(personService.updateStatus(id, status));
    }
}
