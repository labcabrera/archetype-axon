package org.labcabrera.sample.archetype.casestep.interfaces.http;

import java.util.List;

import org.axonframework.queryhandling.QueryGateway;
import org.labcabrera.sample.archetype.casestep.application.cqrs.queries.GetCaseStepByIdQuery;
import org.labcabrera.sample.archetype.casestep.application.cqrs.queries.GetCaseStepsByCaseFolderIdQuery;
import org.labcabrera.sample.archetype.casestep.domain.aggregates.CaseStepAggregate;
import org.labcabrera.sample.archetype.casestep.interfaces.http.dto.CaseStepDto;
import org.labcabrera.sample.archetype.casestep.interfaces.http.mappers.CaseStepDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CaseStepController implements CaseStepControllerDefinition {

    private final QueryGateway queryBus;
    private final CaseStepDtoMapper mapper;

    @Override
    public ResponseEntity<CaseStepDto> getCaseStepById(String caseStepId) {
        var query = new GetCaseStepByIdQuery(caseStepId);
        CaseStepAggregate caseStep = queryBus.query(query, CaseStepAggregate.class).join();
        var dto = mapper.toDto(caseStep);
        return ResponseEntity.ok(dto);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<CaseStepDto>> getCaseStepsByCaseFolderId(String caseFolderId) {
        var query = new GetCaseStepsByCaseFolderIdQuery(caseFolderId);
        List<CaseStepAggregate> caseSteps = queryBus.query(query, List.class).join();
        var dtos = caseSteps.stream().map(step -> mapper.toDto(step)).toList();
        return ResponseEntity.ok(dtos);
    }

}
