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

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aggregate
@Entity
@Table(name = "case_folder")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class CaseFolderAggregate {

    @AggregateIdentifier
    @Id
    @Column(name = "id", length = 36)
    @NotNull
    private String id;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    @NotNull
    private CaseFolderStatus status;

    @Column(name = "name", nullable = false, length = 100)
    @NotNull
    private String name;

    @Column(name = "first_surname", nullable = false, length = 100)
    @NotNull
    private String firstSurname;

    @Column(name = "last_surname", length = 100)
    private String lastSurname;

    @Embedded
    @NotNull
    private IdCard idCard;

    @Column(name = "owner", nullable = false, length = 100)
    @NotNull
    private String owner;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CommandHandler
    public CaseFolderAggregate(CreateCaseFolderCommand command) {
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