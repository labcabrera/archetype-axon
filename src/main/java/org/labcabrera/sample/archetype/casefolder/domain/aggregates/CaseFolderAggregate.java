package org.labcabrera.sample.archetype.casefolder.domain.aggregates;

import java.time.LocalDateTime;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.CreateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.UpdateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderCreatedEvent;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderUpdatedEvent;
import org.labcabrera.sample.archetype.casefolder.domain.valueobjects.CaseFolderStatus;
import org.labcabrera.sample.archetype.casefolder.domain.valueobjects.IdCard;

import jakarta.validation.constraints.NotNull;
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
public class CaseFolderAggregate {

    @AggregateIdentifier
    @NotNull
    private String id;

    @NotNull
    private CaseFolderStatus status;

    @NotNull
    private String name;

    @NotNull
    private String firstSurname;

    private String lastSurname;

    @NotNull
    private IdCard idCard;

    @NotNull
    private String owner;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @CommandHandler
    public CaseFolderAggregate(CreateCaseFolderCommand command) {
        log.info("Creating case folder aggregate {}", command.idCardNumber());
        this.id = UUID.randomUUID().toString();
        this.status = CaseFolderStatus.ACTIVE;
        this.name = command.name();
        this.firstSurname = command.firstSurname();
        this.lastSurname = command.lastSurname();
        this.idCard = new IdCard(command.idCardType(), command.idCardNumber());
        this.createdAt = LocalDateTime.now();
        this.owner = command.username();
        this.normalize();
        AggregateLifecycle.apply(new CaseFolderCreatedEvent(this.id, this.status, this.name,
            this.firstSurname, this.lastSurname, this.idCard, this.owner, this.createdAt));
    }

    @CommandHandler
    public void handle(UpdateCaseFolderCommand command) {
        log.info("Updating case folder aggregate {}", command.caseFolderId());
        this.name = command.name();
        this.firstSurname = command.firstSurname();
        this.lastSurname = command.lastSurname();
        this.updatedAt = LocalDateTime.now();
        this.normalize();
        AggregateLifecycle.apply(new CaseFolderUpdatedEvent(this.id, this.name,
            this.firstSurname, this.lastSurname, this.updatedAt));
    }

    public CaseFolderAggregate normalize() {
        name = name.toUpperCase();
        firstSurname = firstSurname.toUpperCase();
        if (lastSurname != null) {
            lastSurname = lastSurname.toUpperCase();
        }
        return this;
    }

}