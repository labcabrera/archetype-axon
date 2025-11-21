package org.labcabrera.sample.archetype.casefolder.domain.events;

import java.time.LocalDateTime;

public record CaseFolderUpdatedEvent(
    String id,
    String name,
    String firstSurname,
    String lastSurname,
    LocalDateTime updatedAt) {
}
