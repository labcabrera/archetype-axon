package org.labcabrera.sample.archetype.casefolder.application.sagas;

import java.time.Duration;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.CompleteCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderCreatedEvent;
import org.labcabrera.sample.archetype.casestep.application.cqrs.commands.CreateInitialCaseStepCommand;
import org.labcabrera.sample.archetype.casestep.domain.events.CaseStepCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

/**
 * NOTA: En este ejemplo, al procesarse dentro del mismo unit-of-work, el evento
 * CaseFolderCreatedEvent y el envío del comando CreateInitialCaseStepCommand necesitamos
 * añadir el DeadlineManager al saga para evitar problemas de sincronización.
 */
@Saga
@Slf4j
@SuppressWarnings("null")
public class CaseFolderCreationSaga {

    private static final String COMPLETE_CASE_FOLDER_DEADLINE = "complete-case-folder";

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient DeadlineManager deadlineManager;

    private String caseFolderId;

    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    public void handle(CaseFolderCreatedEvent event) {
        log.info("[SAGA] Starting saga for case folder creation: {}", event.id());

        this.caseFolderId = event.id();
        SagaLifecycle.associateWith("caseFolderId", event.id());
        log.debug("[SAGA] Associated saga with caseFolderId: {}", event.id());

        CreateInitialCaseStepCommand command = new CreateInitialCaseStepCommand(
            event.id(),
            event.owner());
        log.debug("[SAGA] Sending command to create initial case step for case folder: {}", event.id());
        commandGateway.send(command);
    }

    @SagaEventHandler(associationProperty = "caseFolderId")
    public void handle(CaseStepCreatedEvent event) {
        log.info("[SAGA] Case step created successfully: {}. Scheduling case folder completion: {}",
            event.caseStepId(), event.caseFolderId());

        Duration delay = Duration.ofMillis(100);
        deadlineManager.schedule(delay, COMPLETE_CASE_FOLDER_DEADLINE);
        log.debug("[SAGA] Scheduled deadline to complete case folder: {}", event.caseFolderId());
    }

    @DeadlineHandler(deadlineName = COMPLETE_CASE_FOLDER_DEADLINE)
    @EndSaga
    public void handleCompleteCaseFolderDeadline() {
        log.info("[SAGA] Executing deadline handler to complete case folder: {}", caseFolderId);
        CompleteCaseFolderCommand command = new CompleteCaseFolderCommand(caseFolderId);
        commandGateway.send(command);
    }
}
