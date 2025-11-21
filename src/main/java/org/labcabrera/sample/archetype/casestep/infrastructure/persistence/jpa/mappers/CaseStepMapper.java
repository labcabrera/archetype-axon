package org.labcabrera.sample.archetype.casestep.infrastructure.persistence.jpa.mappers;

import org.labcabrera.sample.archetype.casestep.domain.aggregates.CaseStepAggregate;
import org.labcabrera.sample.archetype.casestep.infrastructure.persistence.jpa.entities.CaseStepEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.mappers.CaseFolderMapper;

@Mapper(componentModel = "spring", uses = { CaseFolderMapper.class })
public interface CaseStepMapper {

    CaseStepAggregate toDomain(CaseStepEntity entity);

    @Mapping(target = "version", ignore = true)
    CaseStepEntity toEntity(CaseStepAggregate domain);

}
