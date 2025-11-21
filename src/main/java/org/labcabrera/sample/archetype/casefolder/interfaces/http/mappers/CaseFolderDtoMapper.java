package org.labcabrera.sample.archetype.casefolder.interfaces.http.mappers;

import org.labcabrera.sample.archetype.casefolder.domain.aggregates.CaseFolderAggregate;
import org.labcabrera.sample.archetype.casefolder.interfaces.http.dto.CaseFolderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { IdCardDtoMapper.class })
public interface CaseFolderDtoMapper {

    CaseFolderDto toDto(CaseFolderAggregate domain);

}
