package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import org.axonframework.commandhandling.CommandHandler;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.DeleteCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.aggregates.CaseFolderAggregate;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderDeletedEvent;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.domain.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteCaseFolderCommandHandler {

    private final CaseFolderRepository caseFolderRepository;
    private final Guard<CaseFolderAggregate> caseFolderGuard;
    private final SecurityPort securityPort;

    @CommandHandler
    public Void handle(DeleteCaseFolderCommand command) {
        var user = securityPort.requireCurrentUser();
        log.debug("Deleting case folder {} (user: {})", command.caseFolderId(), user.username());
        var caseFolder = caseFolderRepository.findById(command.caseFolderId())
            .orElseThrow(() -> new NotFoundException("case-folder.msg.not-found", command.caseFolderId(), CaseFolderAggregate.class));
        caseFolderGuard.checkWrite(caseFolder, user);
        caseFolderRepository.deleteById(command.caseFolderId());
        return null;
    }

}
