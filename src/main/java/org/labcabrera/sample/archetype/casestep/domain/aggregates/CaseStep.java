package org.labcabrera.sample.archetype.casestep.domain.aggregates;

import java.time.LocalDateTime;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderCreatedEvent;
import org.labcabrera.sample.archetype.casestep.application.cqrs.commands.CreateInitialCaseStepCommand;
import org.labcabrera.sample.archetype.casestep.domain.aggregates.valueobjects.StepStatus;
import org.labcabrera.sample.archetype.casestep.domain.aggregates.valueobjects.StepType;
import org.springframework.cglib.core.Local;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aggregate
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class CaseStep {

    private String id;

    private String caseFolderId;

    private StepType stepType;

    private StepStatus status;

    private String assignedTo;

    private String owner;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @CommandHandler
    public CaseStep(CreateInitialCaseStepCommand event) {
        log.debug("Creating case step from case folder created event {}", event);
        this.id = UUID.randomUUID().toString();
        this.caseFolderId = event.caseFolderId();
        this.stepType = StepType.INITIAL_REVIEW;
        this.status = StepStatus.IN_PROGRESS;
        this.assignedTo = event.owner();
        this.owner = event.owner();
        this.createdAt = LocalDateTime.now();
    }

}
