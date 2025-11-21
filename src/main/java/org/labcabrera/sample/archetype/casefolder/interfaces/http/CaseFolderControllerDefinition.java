package org.labcabrera.sample.archetype.casefolder.interfaces.http;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.kafka.common.requests.ApiError;
import org.labcabrera.sample.archetype.casefolder.interfaces.http.dto.CreateCaseFolderRequest;
import org.labcabrera.sample.archetype.casefolder.interfaces.http.dto.CaseFolderDto;
import org.labcabrera.sample.archetype.casefolder.interfaces.http.dto.UpdateCaseFolderRequest;
import org.labcabrera.sample.archetype.shared.interfaces.http.PageResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1/case-folders")
@Tag(name = "Case Folders", description = "API for case folder management")
public interface CaseFolderControllerDefinition {

    @GetMapping("/{caseFolderId}")
    @Operation(summary = "Get case folder by id", description = "Retrieve a specific case folder by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Case folder found", content = @Content(schema = @Schema(implementation = CaseFolderDto.class))),
        @ApiResponse(responseCode = "404", description = "Case folder not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<CaseFolderDto> getCaseFolderById(
        @Parameter(description = "Unique case folder identifier", required = true) @PathVariable String caseFolderId);

    @GetMapping
    @Operation(summary = "Get case folders by RSQL", description = "Filter case folders using an RSQL expression with optional pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Case folders list retrieved successfully", content = @Content(schema = @Schema(implementation = PageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid RSQL expression", content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    ResponseEntity<PageResponse<CaseFolderDto>> getCaseFoldersByRsql(
        @Parameter(description = "RSQL expression to filter case folders", name = "q", required = false) @RequestParam(required = false, name = "q") String rsql,
        @ParameterObject Pageable pageable);

    @PostMapping
    @Operation(summary = "Create new case folder", description = "Creates a new case folder using the CQRS pattern. Sends a command that emits an event.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Case folder created successfully. Location header contains the URI of the created resource."),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<Void> create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Case folder data to create", required = true, content = @Content(schema = @Schema(implementation = CreateCaseFolderRequest.class))) @RequestBody CreateCaseFolderRequest request);

    @PatchMapping("/{caseFolderId}")
    @Operation(summary = "Update case folder", description = "Updates an existing case folder using the CQRS pattern. Sends a command that emits an event.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Case folder updated successfully", content = @Content(schema = @Schema(implementation = CaseFolderDto.class))),
        @ApiResponse(responseCode = "404", description = "Case folder not found", content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<CaseFolderDto> update(
        @Parameter(description = "Unique case folder identifier", required = true) @PathVariable String caseFolderId,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Case folder data to update", required = true) @RequestBody UpdateCaseFolderRequest request);

    @DeleteMapping("/{caseFolderId}")
    @Operation(summary = "Delete case folder", description = "Deletes an existing case folder using the CQRS pattern. Sends a command that emits an event.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Case folder deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Case folder not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<Void> delete(
        @Parameter(description = "Unique case folder identifier", required = true) @PathVariable String caseFolderId);

}
