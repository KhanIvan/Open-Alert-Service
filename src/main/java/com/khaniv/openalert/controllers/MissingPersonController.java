package com.khaniv.openalert.controllers;

import com.khaniv.openalert.documents.MissingPerson;
import com.khaniv.openalert.documents.enums.MissingPersonType;
import com.khaniv.openalert.services.MissingPersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/person")
@Log4j2
public class MissingPersonController {
    private final MissingPersonService missingPersonService;

    @GetMapping("/lost")
    @ResponseBody
    public List<MissingPerson> findLostPersons() {
        return missingPersonService.findByTypeAndActive(MissingPersonType.LOST, true);
    }

    @GetMapping("/seen")
    @ResponseBody
    public List<MissingPerson> findSeenPersons() {
        return missingPersonService.findByTypeAndActive(MissingPersonType.SEEN, true);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public MissingPerson findMissingPerson(@PathVariable(value = "id") UUID id) {
        return missingPersonService.findById(id);
    }

    @PostMapping
    @ResponseBody
    public MissingPerson save(@RequestBody MissingPerson missingPerson) {
        return missingPersonService.save(missingPerson);
    }

    @PutMapping("/description")
    @ResponseBody
    public MissingPerson updateDescription(@RequestBody MissingPerson source) {
        return missingPersonService.updateDescription(source);
    }

    @PutMapping("/status")
    @ResponseBody
    public MissingPerson updateStatus(@RequestBody MissingPerson source) {
        return missingPersonService.updateStatus(source);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable(value = "id") UUID id) {
        missingPersonService.delete(id);
    }

}
