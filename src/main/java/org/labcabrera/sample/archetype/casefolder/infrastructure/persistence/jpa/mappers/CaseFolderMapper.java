package org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.mappers;

import org.labcabrera.sample.archetype.casefolder.domain.aggregates.CaseFolderAggregate;
import org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities.CaseFolderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CaseFolderMapper {

    CaseFolderAggregate toDomain(CaseFolderEntity entity);

    @Mapping(target = "version", ignore = true)
    CaseFolderEntity toEntity(CaseFolderAggregate domain);

}
