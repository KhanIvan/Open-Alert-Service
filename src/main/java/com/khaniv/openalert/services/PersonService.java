package com.khaniv.openalert.services;

import com.khaniv.openalert.documents.Person;
import com.khaniv.openalert.enums.PersonType;
import com.khaniv.openalert.enums.SearchStatus;
import com.khaniv.openalert.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findById(UUID id) {
        return personRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("No person with ID " + id + " found!")
        );
    }

    public List<Person> findByTypeAndActive(PersonType type, Boolean active) {
        return personRepository.findByTypeAndActive(type, active);
    }

    @Transactional
    public Person save(Person person) {
        if(Objects.isNull(person.getId()) || !personRepository.existsById(person.getId())) {
            return this.insert(person);
        } else {
            return this.update(person);
        }
    }

    @Transactional
    public Person insert(Person person) {
        person.setId(UUID.randomUUID());
        person.setActive(true);
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setMatches(new ArrayList<>());
        person.setStatus(SearchStatus.LOST);
        return personRepository.save(person);
    }

    @Transactional
    public Person update(Person newPerson) {
        Person person = changeInformation(newPerson);
        return personRepository.save(person);
    }

    private Person changeInformation(Person newPerson) {
        Person originalPerson = this.findById(newPerson.getId());
        originalPerson.setDescription(newPerson.getDescription());
        originalPerson.setDetails(newPerson.getDetails());
        originalPerson.setUpdatedAt(LocalDateTime.now());
        return originalPerson;
    }

    @Transactional
    public void deleteAll() {
        personRepository.deleteAll();
    }

    @Transactional
    public void delete(UUID id) {
        personRepository.deleteById(id);
    }

    @Transactional
    public Person updateActive(UUID id, Boolean active) {
        Person person = this.findById(id);
        person.setActive(active);
        person.setUpdatedAt(LocalDateTime.now());
        return personRepository.save(person);
    }

    @Transactional
    public Person updateStatus(UUID id, SearchStatus status) {
        Person person = this.findById(id);
        person.setStatus(status);
        person.setUpdatedAt(LocalDateTime.now());
        return personRepository.save(person);
    }
}
