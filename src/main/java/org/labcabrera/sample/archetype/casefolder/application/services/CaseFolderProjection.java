package org.labcabrera.sample.archetype.casefolder.application.services;

import org.axonframework.eventhandling.EventHandler;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.aggregates.CaseFolderAggregate;
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

    // @EventHandler
    // public void on(CaseFolderCreatedEvent event) {
    //     log.info("Saving case folder {}", event.id());
    //     CaseFolderAggregate aggregate = CaseFolderAggregate.builder()
    //         .id(event.id())
    //         .status(event.status())
    //         .name(event.name())
    //         .firstSurname(event.firstSurname())
    //         .lastSurname(event.lastSurname())
    //         .idCard(event.idCard())
    //         .owner(event.owner())
    //         .createdAt(event.createdAt())
    //         .build();
    //     caseFolderRepository.save(aggregate);
    // }

    // @EventHandler
    // public void on(CaseFolderUpdatedEvent event) {
    //     log.info("Updating case folder {}", event.id());
    //     caseFolderRepository.findById(event.id()).ifPresent(aggregate -> {
    //         aggregate.setName(event.name());
    //         aggregate.setFirstSurname(event.firstSurname());
    //         aggregate.setLastSurname(event.lastSurname());
    //         aggregate.setUpdatedAt(event.updatedAt());
    //         caseFolderRepository.update(aggregate);
    //     });
    // }

}
