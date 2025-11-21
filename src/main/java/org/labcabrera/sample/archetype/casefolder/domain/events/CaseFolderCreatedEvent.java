package org.labcabrera.sample.archetype.casefolder.domain.events;

import java.time.LocalDateTime;

import org.labcabrera.sample.archetype.casefolder.domain.valueobjects.CaseFolderStatus;
import org.labcabrera.sample.archetype.casefolder.domain.valueobjects.IdCard;

public record CaseFolderCreatedEvent(
    String id,
    CaseFolderStatus status,
    String name,
    String firstSurname,
    String lastSurname,
    IdCard idCard,
    String owner,
    LocalDateTime createdAt) {
}
