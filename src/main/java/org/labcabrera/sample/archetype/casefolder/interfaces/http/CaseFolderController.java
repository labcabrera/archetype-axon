package org.labcabrera.sample.archetype.casefolder.interfaces.http;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.CreateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.DeleteCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.UpdateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.queries.GetCaseFolderByIdQuery;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.queries.GetCaseFoldersByRsqlQuery;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.interfaces.http.dto.CreateCaseFolderRequest;
import org.labcabrera.sample.archetype.casefolder.interfaces.http.dto.CaseFolderDto;
import org.labcabrera.sample.archetype.casefolder.interfaces.http.dto.UpdateCaseFolderRequest;
import org.labcabrera.sample.archetype.shared.interfaces.http.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.labcabrera.sample.archetype.casefolder.interfaces.http.mappers.CaseFolderDtoMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("null")
public class CaseFolderController implements CaseFolderControllerDefinition {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final CaseFolderDtoMapper mapper;

    @Override
    public ResponseEntity<CaseFolderDto> getCaseFolderById(@PathVariable String caseFolderId) {
        var query = new GetCaseFolderByIdQuery(caseFolderId);
        CaseFolder caseFolder = queryGateway.query(query, CaseFolder.class).join();
        var caseFolderDto = mapper.toDto(caseFolder);
        return ResponseEntity.ok(caseFolderDto);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<PageResponse<CaseFolderDto>> getCaseFoldersByRsql(String rsql, Pageable pageable) {
        var query = new GetCaseFoldersByRsqlQuery(rsql, pageable);
        Page<CaseFolder> page = queryGateway.query(query, ResponseTypes.instanceOf(Page.class)).join();
        var pageDto = page.map(caseFolder -> mapper.toDto(caseFolder));
        var response = new PageResponse<>(pageDto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CaseFolderDto> create(@RequestBody @Validated CreateCaseFolderRequest request) {
        var command = new CreateCaseFolderCommand(
            request.name(),
            request.firstSurname(),
            request.lastSurname(),
            request.idCard().type(),
            request.idCard().number());
        CaseFolder caseFolder = commandGateway.sendAndWait(command);
        var caseFolderDto = mapper.toDto(caseFolder);
        return ResponseEntity.status(201).body(caseFolderDto);
    }

    @Override
    public ResponseEntity<CaseFolderDto> update(String caseFolderId, UpdateCaseFolderRequest request) {
        var command = new UpdateCaseFolderCommand(
            caseFolderId,
            request.name(),
            request.firstSurname(),
            request.lastSurname());
        CaseFolder caseFolder = commandGateway.sendAndWait(command);
        var caseFolderDto = mapper.toDto(caseFolder);
        return ResponseEntity.ok(caseFolderDto);
    }

    @Override
    public ResponseEntity<Void> delete(String caseFolderId) {
        var command = new DeleteCaseFolderCommand(caseFolderId);
        commandGateway.sendAndWait(command);
        return ResponseEntity.noContent().build();
    }

}
