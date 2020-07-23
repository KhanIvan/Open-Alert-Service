package com.khaniv.openalert.mappers;

import com.khaniv.openalert.MatchDto;
import com.khaniv.openalert.documents.Match;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchMapper {
    Match toDocument(MatchDto dto);

    List<Match> toDocument(List<MatchDto> dtos);

    MatchDto toDto(Match document);

    List<MatchDto> toDto(List<Match> documents);
}