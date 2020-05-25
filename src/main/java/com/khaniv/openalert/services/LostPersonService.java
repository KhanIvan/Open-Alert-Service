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
    private final LostPersonRepository lostPersonRepository;

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
        LostPerson ret = lostPerson;
        if (Objects.isNull(lostPerson.getId()) || !lostPersonRepository.existsById(lostPerson.getId())) {
            ret.setStatus(LostPersonStatus.builder()
                    .active(true)
                    .closeMatches(new ArrayList<>())
                    .presumablyFoundIds(new ArrayList<>())
                    .foundIds(new ArrayList<>())
                    .status(SearchStatus.LOST)
                    .build());
            ret.setId(UUID.randomUUID());
            ret.setCreatedAt(LocalDateTime.now());
        } else {
            ret = updateDescription(lostPerson);
        }
        ret.setUpdatedAt(LocalDateTime.now());
        return ret;
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
