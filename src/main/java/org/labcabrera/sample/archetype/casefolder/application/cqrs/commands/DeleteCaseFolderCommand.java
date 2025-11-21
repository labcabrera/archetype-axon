package org.labcabrera.sample.archetype.casefolder.application.cqrs.commands;

import java.util.Set;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record DeleteCaseFolderCommand(

    @TargetAggregateIdentifier String caseFolderId,

    String username,

    Set<String> roles

) {
}
