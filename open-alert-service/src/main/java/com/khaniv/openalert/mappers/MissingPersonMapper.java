package com.khaniv.openalert.mappers;

import com.khaniv.openalert.MissingPersonDto;
import com.khaniv.openalert.documents.MissingPerson;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MissingPersonMapper {
    MissingPerson toDocument(MissingPersonDto dto);

    List<MissingPerson> toDocument(List<MissingPersonDto> dtos);

    MissingPersonDto toDto(MissingPerson document);

    List<MissingPersonDto> toDto(List<MissingPerson> documents);
}
