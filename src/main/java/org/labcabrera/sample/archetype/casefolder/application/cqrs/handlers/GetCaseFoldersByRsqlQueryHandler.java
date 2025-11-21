package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import org.axonframework.queryhandling.QueryHandler;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.queries.GetCaseFoldersByRsqlQuery;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.aggregates.CaseFolderPage;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetCaseFoldersByRsqlQueryHandler {

    private final CaseFolderRepository caseFolderRepository;
    private final SecurityPort securityPort;

    @QueryHandler
    public CaseFolderPage handle(GetCaseFoldersByRsqlQuery query) {
        var user = securityPort.requireCurrentUser();
        log.debug("Getting case folders by RSQL <<< {} (user: {})", query.rsql(), user.username());
        return caseFolderRepository.findByRsql(query.rsql(), query.pageable(), user);
    }
}
