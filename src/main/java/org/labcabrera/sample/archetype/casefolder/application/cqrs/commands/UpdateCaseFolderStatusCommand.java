package org.labcabrera.sample.archetype.casefolder.application.cqrs.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.labcabrera.sample.archetype.casefolder.domain.valueobjects.CaseFolderStatus;

public record UpdateCaseFolderStatusCommand(

    @TargetAggregateIdentifier String caseFolderId,

    CaseFolderStatus status

) {
}
