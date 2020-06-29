package com.khaniv.openalert.services;

import com.khaniv.openalert.documents.MissingPerson;
import com.khaniv.openalert.documents.data.MissingPersonStatus;
import com.khaniv.openalert.documents.enums.MissingPersonType;
import com.khaniv.openalert.documents.enums.SearchStatus;
import com.khaniv.openalert.errors.DocumentNotFoundException;
import com.khaniv.openalert.repositories.MissingPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissingPersonService {
    private final MissingPersonRepository missingPersonRepository;

    public MissingPerson findById(UUID id) {
        return missingPersonRepository.findById(id).orElseThrow(
                () -> new DocumentNotFoundException(MissingPerson.class, id)
        );
    }

    public List<MissingPerson> findByTypeAndActive(MissingPersonType type, Boolean active) {
        return missingPersonRepository.findByTypeAndActive(type, active);
    }

    @Transactional
    public MissingPerson save(MissingPerson missingPerson) {
        return missingPersonRepository.save(generateMissingPerson(missingPerson));
    }

    @Transactional
    public void delete(UUID id) {
        missingPersonRepository.deleteById(id);
    }

    @Transactional
    public MissingPerson updateDescription(MissingPerson source) {
        MissingPerson missingPerson = findById(source.getId());
        missingPerson.setDescription(source.getDescription());
        return missingPersonRepository.save(missingPerson);
    }

    @Transactional
    public MissingPerson updateStatus(MissingPerson source) {
        MissingPerson missingPerson = findById(source.getId());
        missingPerson.setStatus(source.getStatus());
        return missingPersonRepository.save(missingPerson);
    }

    @Transactional
    public MissingPerson updateActive(UUID id, boolean active) {
        MissingPerson missingPerson = findById(id);
        missingPerson.setActive(active);
        return missingPersonRepository.save(missingPerson);
    }

    public boolean existsById(UUID id) {
        return missingPersonRepository.existsById(id);
    }

    private MissingPerson generateMissingPerson(MissingPerson missingPerson) {
        return MissingPerson.builder()
                .id(UUID.randomUUID())
                .active(true)
                .description(missingPerson.getDescription())
                .type(missingPerson.getType())
                .status(generateMissingPersonStatus(missingPerson))
                .build();
    }

    private MissingPersonStatus generateMissingPersonStatus(MissingPerson missingPerson) {
        return MissingPersonStatus.builder()
                .lostAt(missingPerson.getStatus().getLostAt())
                .status(SearchStatus.LOST)
                .build();
    }
}
