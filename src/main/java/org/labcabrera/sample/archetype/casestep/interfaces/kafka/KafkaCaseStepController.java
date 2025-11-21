package org.labcabrera.sample.archetype.casestep.interfaces.kafka;

import java.util.function.Consumer;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderCreatedEvent;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderDeletedEvent;
import org.labcabrera.sample.archetype.casestep.application.cqrs.commands.CreateInitialCaseStepCommand;
import org.labcabrera.sample.archetype.casestep.application.cqrs.commands.DeleteCaseStepsByCaseFolderIdCommand;
import org.labcabrera.sample.archetype.shared.infrastructure.messaging.kafka.AuthenticatedConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaCaseStepController extends AuthenticatedConsumer {

    private final CommandGateway commandBus;

    @Bean
    public Consumer<Message<CaseFolderCreatedEvent>> onCaseFolderCreated() {
        return message -> {
            log.debug("Received case folder created event: {}", message.getPayload().id());
            try {
                loadUserContext(message);
                var command = new CreateInitialCaseStepCommand(message.getPayload().id());
                commandBus.send(command);
            }
            finally {
                SecurityContextHolder.clearContext();
            }
        };
    }

    @Bean
    public Consumer<Message<CaseFolderDeletedEvent>> onCaseFolderDeleted() {
        return message -> {
            log.debug("Received case folder created event: {}", message.getPayload().caseFolderId());
            loadUserContext(message);
            try {
                var caseFolderId = message.getPayload().caseFolderId();
                var command = new DeleteCaseStepsByCaseFolderIdCommand(caseFolderId);
                commandBus.send(command);
            }
            finally {
                SecurityContextHolder.clearContext();
            }
        };
    }

}