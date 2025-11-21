package org.labcabrera.sample.archetype.casefolder.interfaces.http.mappers;

import org.labcabrera.sample.archetype.casefolder.domain.valueobjects.IdCard;
import org.labcabrera.sample.archetype.casefolder.interfaces.http.dto.IdCardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IdCardDtoMapper {

    @Mapping(source = "idCardType", target = "type")
    @Mapping(source = "idCardNumber", target = "number")
    IdCardDto toDto(IdCard domain);

}
