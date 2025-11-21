package org.labcabrera.sample.archetype.shared.infrastructure.configuration;

import org.axonframework.common.lock.LockFactory;
import org.axonframework.common.lock.PessimisticLockFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.modelling.command.Repository;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.aggregates.CaseFolderAggregate;
import org.labcabrera.sample.archetype.casefolder.infrastructure.axon.StateStoredCaseFolderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class AxonConfiguration {

    @Bean
    public LockFactory lockFactory() {
        return PessimisticLockFactory.usingDefaults();
    }

    @Bean
    public Repository<CaseFolderAggregate> caseFolderAggregateRepository(
        CaseFolderRepository caseFolderRepository,
        LockFactory lockFactory,
        EventBus eventBus) {
        return new StateStoredCaseFolderRepository(caseFolderRepository, lockFactory, eventBus);
    }

    @Bean
    public Serializer eventSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return JacksonSerializer.builder()
            .objectMapper(objectMapper)
            .build();
    }
}