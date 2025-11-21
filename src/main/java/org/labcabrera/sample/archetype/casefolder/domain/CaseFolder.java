package org.labcabrera.sample.archetype.casefolder.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.CreateCaseFolderCommand;

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
public class CaseFolder {

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
    public CaseFolder(CreateCaseFolderCommand command) {
        this.id = UUID.randomUUID().toString();
        this.status = CaseFolderStatus.ACTIVE;
        this.name = command.name();
        this.firstSurname = command.firstSurname();
        this.lastSurname = command.lastSurname();
        this.idCard = new IdCard(command.idCardType(), command.idCardNumber());
        this.createdAt = LocalDateTime.now();
        this.owner = command.owner();
    }

    public CaseFolder normalize() {
        name = name.toUpperCase();
        firstSurname = firstSurname.toUpperCase();
        if (lastSurname != null) {
            lastSurname = lastSurname.toUpperCase();
        }
        return this;
    }

}