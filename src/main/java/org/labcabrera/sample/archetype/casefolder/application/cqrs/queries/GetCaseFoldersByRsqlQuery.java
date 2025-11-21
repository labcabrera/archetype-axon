package org.labcabrera.sample.archetype.casefolder.application.cqrs.queries;

import java.util.Set;

import org.springframework.data.domain.Pageable;

public record GetCaseFoldersByRsqlQuery(
    String rsql,
    Pageable pageable,
    String username,
    Set<String> roles) {
}
