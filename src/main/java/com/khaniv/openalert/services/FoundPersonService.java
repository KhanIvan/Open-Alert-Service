package com.khaniv.openalert.services;

import com.khaniv.openalert.documents.FoundPerson;
import com.khaniv.openalert.documents.FoundPersonStatus;
import com.khaniv.openalert.documents.enums.FoundStatus;
import com.khaniv.openalert.repositories.FoundPersonRepository;
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
public class FoundPersonService {
    private final FoundPersonRepository foundPersonRepository;

    public List<FoundPerson> findAll() {
        return foundPersonRepository.findAll();
    }

    public FoundPerson findById(UUID id) {
        return foundPersonRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Found person with ID " + id + " not found!")
        );
    }

    @Transactional
    public FoundPerson save(FoundPerson foundPerson) {
        foundPerson = prepareData(foundPerson);
        return foundPersonRepository.save(foundPerson);
    }

    private FoundPerson prepareData(FoundPerson foundPerson) {
        FoundPerson ret = foundPerson;
        if (Objects.isNull(foundPerson.getId()) || !foundPersonRepository.existsById(foundPerson.getId())) {
            ret.setStatus(FoundPersonStatus.builder()
                    .active(true)
                    .closeMatches(new ArrayList<>())
                    .presumablyFoundIds(new ArrayList<>())
                    .status(FoundStatus.NO_MATCHES)
                    .build());
            ret.setCreatedAt(LocalDateTime.now());
            ret.setId(UUID.randomUUID());
        } else {
            ret = updatePersonDescription(foundPerson);
        }

        ret.setUpdatedAt(LocalDateTime.now());
        return ret;
    }

    private FoundPerson updatePersonDescription(FoundPerson foundPerson) {
        FoundPerson originalFoundPerson = findById(foundPerson.getId());
        originalFoundPerson.setDescription(foundPerson.getDescription());
        originalFoundPerson.setDetails(foundPerson.getDetails());
        return originalFoundPerson;
    }

    @Transactional
    public void deleteAll() {
        foundPersonRepository.deleteAll();
    }

    @Transactional
    public void delete(UUID id) {
        foundPersonRepository.deleteById(id);
    }
}
