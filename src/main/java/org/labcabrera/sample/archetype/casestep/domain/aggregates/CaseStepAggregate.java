package org.labcabrera.sample.archetype.casestep.domain.aggregates;

import java.time.LocalDateTime;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.labcabrera.sample.archetype.casestep.application.cqrs.commands.CreateInitialCaseStepCommand;
import org.labcabrera.sample.archetype.casestep.domain.aggregates.valueobjects.StepStatus;
import org.labcabrera.sample.archetype.casestep.domain.aggregates.valueobjects.StepType;
import org.labcabrera.sample.archetype.casestep.domain.events.CaseStepCreatedEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aggregate(repository = "caseStepAggregateRepository")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class CaseStepAggregate {

    @AggregateIdentifier
    private String id;

    private String caseFolderId;

    private StepType stepType;

    private StepStatus status;

    private String assignedTo;

    private String owner;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @CommandHandler
    public CaseStepAggregate(CreateInitialCaseStepCommand command) {
        log.debug("Creating case step for case folder {}", command.caseFolderId());
        this.id = UUID.randomUUID().toString();
        this.caseFolderId = command.caseFolderId();
        this.stepType = StepType.INITIAL_REVIEW;
        this.status = StepStatus.IN_PROGRESS;
        this.assignedTo = command.owner();
        this.owner = command.owner();
        this.createdAt = LocalDateTime.now();
        AggregateLifecycle.apply(new CaseStepCreatedEvent(
            this.id,
            this.caseFolderId));
    }

}
