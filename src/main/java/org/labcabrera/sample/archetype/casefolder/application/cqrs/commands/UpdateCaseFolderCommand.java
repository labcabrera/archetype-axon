package org.labcabrera.sample.archetype.casefolder.application.cqrs.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record UpdateCaseFolderCommand(

    @TargetAggregateIdentifier String caseFolderId,

    String name,

    String firstSurname,

    String lastSurname) {
}
