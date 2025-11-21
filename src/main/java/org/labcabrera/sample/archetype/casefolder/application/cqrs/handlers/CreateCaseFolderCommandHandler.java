package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import org.axonframework.eventhandling.EventHandler;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.CreateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderEventBusPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.IdCard;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderCreatedEvent;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.domain.exceptions.ConstraintViolationException;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateCaseFolderCommandHandler {

    private final CaseFolderRepository caseFolderRepository;
    private final CaseFolderEventBusPort caseFolderEventBusPort;
    private final SecurityPort securityPort;
    private final Guard<CaseFolder> caseFolderGuard;
    private final Validator validator;
    private final MeterRegistry meterRegistry;
    private Counter caseFolderCreatedCounter;

    @PostConstruct
    private void initMetrics() {
        caseFolderCreatedCounter = Counter.builder("casefoldercreated")
            .description("Number of case folders created")
            .register(meterRegistry);
    }

    @EventHandler
    public CaseFolder on(CreateCaseFolderCommand command) {
        var user = securityPort.requireCurrentUser();
        log.info("Create case folder << {} (user: {})", command.idCardNumber(), user.username());
        caseFolderGuard.checkCreate(user);
        validateCommand(command);
        var caseFolder = buildCaseFolderFromCommand(command, user.username());
        validateCaseFolder(caseFolder);
        var created = caseFolderRepository.save(caseFolder);
        caseFolderCreatedCounter.increment();
        sendNotification(created);
        return created;
    }

    private void validateCommand(CreateCaseFolderCommand command) {
        var violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("case-folder.msg.err.validation-error", violations);
        }
    }

    private void validateCaseFolder(CaseFolder caseFolder) {
        var violations = validator.validate(caseFolder);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("case-folder.msg.err.validation-error", violations);
        }
    }

    private CaseFolder buildCaseFolderFromCommand(CreateCaseFolderCommand command, String username) {
        return CaseFolder.create(
            command.name(),
            command.firstSurname(),
            command.lastSurname(),
            new IdCard(command.idCardNumber(), command.idCardType()), username);
    }

    private void sendNotification(CaseFolder caseFolder) {
        var event = new CaseFolderCreatedEvent(
            caseFolder.getId(),
            caseFolder.getName(),
            caseFolder.getFirstSurname(),
            caseFolder.getLastSurname(),
            caseFolder.getIdCard().idCardType(),
            caseFolder.getIdCard().idCardNumber(),
            caseFolder.getCreatedAt());
        caseFolderEventBusPort.publish(event);
    }

}