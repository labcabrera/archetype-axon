package org.labcabrera.sample.archetype.casestep.interfaces.http.mappers;

import org.labcabrera.sample.archetype.casestep.domain.CaseStep;
import org.labcabrera.sample.archetype.casestep.interfaces.http.dto.CaseStepDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CaseStepDtoMapper {

    CaseStepDto toDto(CaseStep domain);

}
