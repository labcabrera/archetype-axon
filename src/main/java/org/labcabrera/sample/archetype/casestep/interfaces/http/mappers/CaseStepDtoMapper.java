package org.labcabrera.sample.archetype.casestep.interfaces.http.mappers;

import org.labcabrera.sample.archetype.casestep.domain.aggregates.CaseStepAggregate;
import org.labcabrera.sample.archetype.casestep.interfaces.http.dto.CaseStepDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CaseStepDtoMapper {

    CaseStepDto toDto(CaseStepAggregate domain);

}
