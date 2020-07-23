package com.khaniv.openalert.services;

import com.khaniv.openalert.dto.MissingPersonDto;
import com.khaniv.openalert.dto.data.MissingPersonStatus;
import com.khaniv.openalert.documents.MissingPerson;
import com.khaniv.openalert.dto.enums.MissingPersonType;
import com.khaniv.openalert.dto.enums.SearchStatus;
import com.khaniv.openalert.errors.exceptions.DocumentNotFoundException;
import com.khaniv.openalert.mappers.MissingPersonMapper;
import com.khaniv.openalert.repositories.MissingPersonRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissingPersonService {
    private final MissingPersonRepository missingPersonRepository;
    private final MissingPersonMapper missingPersonMapper = Mappers.getMapper(MissingPersonMapper.class);

    public MissingPersonDto findById(UUID id) {
        return missingPersonMapper.toDto(missingPersonRepository.findById(id).orElseThrow(
                () -> new DocumentNotFoundException(MissingPerson.class, id)
        ));
    }

    public List<MissingPersonDto> findByTypeAndActive(MissingPersonType type, Boolean active) {
        return missingPersonMapper.toDto(missingPersonRepository.findByTypeAndActive(type, active));
    }

    @Transactional
    public MissingPersonDto save(MissingPersonDto missingPerson) {
        return missingPersonMapper.toDto(missingPersonRepository.save(generateMissingPerson(missingPerson)));
    }

    @Transactional
    public void delete(UUID id) {
        missingPersonRepository.deleteById(id);
    }

    @Transactional
    public MissingPersonDto updateDescription(MissingPersonDto source) {
        MissingPersonDto missingPerson = findById(source.getId());
        missingPerson.setDescription(source.getDescription());
        return missingPersonMapper.toDto(missingPersonRepository.save(missingPersonMapper.toDocument(missingPerson)));
    }

    @Transactional
    public MissingPersonDto updateStatus(MissingPersonDto source) {
        MissingPersonDto missingPerson = findById(source.getId());
        missingPerson.setStatus(source.getStatus());
        return missingPersonMapper.toDto(missingPersonRepository.save(missingPersonMapper.toDocument(missingPerson)));
    }

    @Transactional
    public MissingPersonDto updateActive(UUID id, boolean active) {
        MissingPersonDto missingPerson = findById(id);
        missingPerson.setActive(active);
        return missingPersonMapper.toDto(missingPersonRepository.save(missingPersonMapper.toDocument(missingPerson)));
    }

    public boolean existsById(UUID id) {
        return missingPersonRepository.existsById(id);
    }

    public boolean existsByIdAndType(UUID id, MissingPersonType type) {
        return missingPersonRepository.findByIdAndType(id, type).isPresent();
    }

    private MissingPerson generateMissingPerson(MissingPersonDto missingPerson) {
        return MissingPerson.builder()
                .description(missingPerson.getDescription())
                .type(missingPerson.getType())
                .status(generateMissingPersonStatus(missingPerson))
                .build();
    }

    private MissingPersonStatus generateMissingPersonStatus(MissingPersonDto missingPerson) {
        return MissingPersonStatus.builder()
                .lostAt(missingPerson.getStatus().getLostAt())
                .status(SearchStatus.LOST)
                .build();
    }
}
