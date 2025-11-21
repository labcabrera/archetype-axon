package org.labcabrera.sample.archetype.casefolder.application.sagas;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.CompleteCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderCreatedEvent;
import org.labcabrera.sample.archetype.casestep.application.cqrs.commands.CreateInitialCaseStepCommand;
import org.labcabrera.sample.archetype.casestep.domain.events.CaseStepCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class CaseFolderCreationSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    public void handle(CaseFolderCreatedEvent event) {
        log.info("[SAGA] Starting saga for case folder creation: {}", event.id());
        CreateInitialCaseStepCommand command = new CreateInitialCaseStepCommand(
            event.id(),
            event.owner());
        log.debug("[SAGA] Sending command to create initial case step for case folder: {}", event.id());
        commandGateway.send(command);
    }

    @SagaEventHandler(associationProperty = "caseFolderId")
    @EndSaga
    public void handle(CaseStepCreatedEvent event) {
        log.info("[SAGA] Case step created successfully: {}. Completing case folder: {}", event.caseStepId(), event.caseFolderId());
        CompleteCaseFolderCommand command = new CompleteCaseFolderCommand(event.caseFolderId());
        log.debug("[SAGA] Sending command to complete case folder: {}", event.caseFolderId());
        commandGateway.send(command);
    }
}
