package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import org.axonframework.commandhandling.CommandHandler;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.UpdateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.aggregates.CaseFolderAggregate;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderUpdatedEvent;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.domain.exceptions.NotFoundException;
import org.labcabrera.sample.archetype.shared.domain.exceptions.NotModifiedException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateCaseFolderCommandHandler {

    private final CaseFolderRepository caseFolderRepository;
    private final Guard<CaseFolderAggregate> caseFolderGuard;
    private final SecurityPort securityPort;

    @CommandHandler
    public CaseFolderAggregate handle(UpdateCaseFolderCommand command) {
        var user = securityPort.requireCurrentUser();
        log.info("Update case folder << {} (user: {})", command.caseFolderId(), user.username());
        var existing = caseFolderRepository.findById(command.caseFolderId())
            .orElseThrow(() -> new NotFoundException("case-folder.msg.not-found", command.caseFolderId(), CaseFolderAggregate.class));
        caseFolderGuard.checkWrite(existing, user);
        merge(existing, command);
        existing.normalize();
        var caseFolder = caseFolderRepository.update(existing);
        return caseFolder;
    }

    private void merge(CaseFolderAggregate existing, UpdateCaseFolderCommand command) {
        boolean modified = false;
        if (command.name() != null && !command.name().toUpperCase().equals(existing.getName())) {
            existing.setName(command.name());
            modified = true;
        }
        if (command.firstSurname() != null && !command.firstSurname().toUpperCase().equals(existing.getFirstSurname())) {
            existing.setFirstSurname(command.firstSurname());
            modified = true;
        }
        if (command.lastSurname() != null && !command.lastSurname().toUpperCase().equals(existing.getLastSurname())) {
            existing.setLastSurname(command.lastSurname());
            modified = true;
        }
        if (!modified) {
            throw new NotModifiedException("case-folder.msg.err.not-modified");
        }
    }
}
