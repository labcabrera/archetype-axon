package org.labcabrera.sample.archetype.shared.infrastructure.configuration;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventMessage;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderCreatedEvent;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderUpdatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class KafkaEventPublisherConfiguration {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public CustomKafkaEventPublisher customKafkaEventPublisher(
        EventBus eventBus,
        KafkaTemplate<String, Object> kafkaTemplate) {
        CustomKafkaEventPublisher publisher = new CustomKafkaEventPublisher(kafkaTemplate);
        eventBus.subscribe(events -> events.forEach(publisher::publish));
        return publisher;
    }

    @RequiredArgsConstructor
    public static class CustomKafkaEventPublisher {
        private final KafkaTemplate<String, Object> kafkaTemplate;

        public void publish(EventMessage<?> eventMessage) {
            Object payload = eventMessage.getPayload();
            String topic = routeEventToTopic(payload);

            log.info("Publishing event {} to topic {}", payload.getClass().getSimpleName(), topic);
            kafkaTemplate.send(topic, eventMessage.getIdentifier(), payload);
        }

        private String routeEventToTopic(Object payload) {
            if (payload instanceof CaseFolderCreatedEvent) {
                return "case-folder-created";
            }
            else if (payload instanceof CaseFolderUpdatedEvent) {
                return "case-folder-updated";
            }
            return "axon-events";
        }
    }
}
