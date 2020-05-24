package com.khaniv.openalert.services;

import com.khaniv.openalert.documents.LostPerson;
import com.khaniv.openalert.documents.LostPersonStatus;
import com.khaniv.openalert.documents.enums.SearchStatus;
import com.khaniv.openalert.repositories.LostPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LostPersonService {
    @Autowired
    private LostPersonRepository lostPersonRepository;

    public List<LostPerson> findAll() {
        return lostPersonRepository.findAll();
    }

    public LostPerson findById(UUID id) {
        return lostPersonRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "No lost person with ID " + id + " found"
        ));
    }

    @Transactional
    public LostPerson save(LostPerson lostPerson) {
        lostPerson = prepareData(lostPerson);
        return lostPersonRepository.save(lostPerson);
    }

    private LostPerson prepareData(LostPerson lostPerson) {
        if (Objects.isNull(lostPerson.getId()) || !lostPersonRepository.existsById(lostPerson.getId())) {
            lostPerson.setStatus(LostPersonStatus.builder()
                    .active(true)
                    .closeMatches(new ArrayList<>())
                    .presumablyFoundIds(new ArrayList<>())
                    .foundIds(new ArrayList<>())
                    .status(SearchStatus.LOST)
                    .build());
            lostPerson.setId(UUID.randomUUID());
            lostPerson.setCreatedAt(LocalDateTime.now());
        } else {
            lostPerson = updateDescription(lostPerson);
        }
        lostPerson.setUpdatedAt(LocalDateTime.now());
        return lostPerson;
    }

    private LostPerson updateDescription(LostPerson lostPerson) {
        LostPerson originalLostPerson = findById(lostPerson.getId());
        originalLostPerson.setDescription(lostPerson.getDescription());
        originalLostPerson.setDetails(lostPerson.getDetails());
        return originalLostPerson;
    }

    @Transactional
    public void deleteAll() {
        lostPersonRepository.deleteAll();
    }

    @Transactional
    public void delete(UUID id) {
        lostPersonRepository.deleteById(id);
    }
}
