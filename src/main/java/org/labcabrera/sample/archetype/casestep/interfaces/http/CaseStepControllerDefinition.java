package org.labcabrera.sample.archetype.casestep.interfaces.http;

import java.util.List;

import org.apache.kafka.common.requests.ApiError;
import org.labcabrera.sample.archetype.casestep.interfaces.http.dto.CaseStepDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api/v1/case-steps")
@Tag(name = "Case Steps", description = "API for case step management")
public interface CaseStepControllerDefinition {

    @GetMapping("/{caseStepId}")
    @Operation(summary = "Get case step by id", description = "Retrieve a specific case step by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Case step found", content = @Content(schema = @Schema(implementation = CaseStepDto.class))),
        @ApiResponse(responseCode = "404", description = "Case step not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<CaseStepDto> getCaseStepById(
        @Parameter(description = "Unique case step identifier", required = true) @PathVariable String caseStepId);

    @GetMapping("/case-folders/{caseFolderId}")
    @Operation(summary = "Get case steps by case folder id", description = "Retrieve case steps associated with a specific case folder")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Case steps found", content = @Content(schema = @Schema(implementation = CaseStepDto.class))),
        @ApiResponse(responseCode = "404", description = "Case steps not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<List<CaseStepDto>> getCaseStepsByCaseFolderId(
        @Parameter(description = "Unique case folder identifier", required = true) @PathVariable String caseFolderId);

}
