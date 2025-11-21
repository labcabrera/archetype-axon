package org.labcabrera.sample.archetype.casefolder.application.services;

import org.axonframework.eventhandling.EventHandler;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderCreatedEvent;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderUpdatedEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CaseFolderProjection {

    private final CaseFolderRepository caseFolderRepository;

    @EventHandler
    public void on(CaseFolderCreatedEvent event) {
        log.info("Saving case folder {}", event.caseFolder().getId());
        caseFolderRepository.save(event.caseFolder());
    }

    @EventHandler
    public void on(CaseFolderUpdatedEvent event) {
        log.info("Updating case folder {}", event.caseFolder().getId());
        caseFolderRepository.save(event.caseFolder());
    }

}
