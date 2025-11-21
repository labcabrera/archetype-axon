package org.labcabrera.sample.archetype.casefolder.application.cqrs.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CompleteCaseFolderCommand(
    @TargetAggregateIdentifier String caseFolderId) {
}
