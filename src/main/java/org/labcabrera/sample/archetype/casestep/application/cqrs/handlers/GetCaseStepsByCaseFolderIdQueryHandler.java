package org.labcabrera.sample.archetype.casestep.application.cqrs.handlers;

import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.labcabrera.sample.archetype.casestep.application.cqrs.queries.GetCaseStepsByCaseFolderIdQuery;
import org.labcabrera.sample.archetype.casestep.application.ports.CaseStepRepository;
import org.labcabrera.sample.archetype.casestep.domain.aggregates.CaseStepAggregate;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetCaseStepsByCaseFolderIdQueryHandler {

    private final CaseStepRepository caseStepRepository;
    private final SecurityPort securityPort;

    @QueryHandler
    public List<CaseStepAggregate> handle(GetCaseStepsByCaseFolderIdQuery query) {
        var user = securityPort.requireCurrentUser();
        log.debug("Getting case steps for case folder {} (user={})", query.caseFolderId(), user.username());
        var caseSteps = caseStepRepository.findByCaseFolderId(query.caseFolderId());
        return caseSteps;
    }

}
