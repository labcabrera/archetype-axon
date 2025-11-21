package org.labcabrera.sample.archetype.casefolder.domain.events;

import org.labcabrera.sample.archetype.casefolder.domain.aggregates.CaseFolderAggregate;

public record CaseFolderUpdatedEvent(CaseFolderAggregate caseFolder) {
}
