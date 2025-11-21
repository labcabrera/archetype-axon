package org.labcabrera.sample.archetype.casefolder.application.cqrs.commands;

import org.labcabrera.sample.archetype.casefolder.domain.valueobjects.CaseFolderStatus;

public record UpdateCaseFolderStatusCommand(
    String caseFolderId,
    CaseFolderStatus status) {
}
