package org.labcabrera.sample.archetype.casestep.interfaces.http.dto;

import org.labcabrera.sample.archetype.casestep.domain.aggregates.valueobjects.StepStatus;
import org.labcabrera.sample.archetype.casestep.domain.aggregates.valueobjects.StepType;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public record CaseStepDto(

    @Schema(description = "Case step identifier", example = "12345", requiredMode = RequiredMode.NOT_REQUIRED) String id,

    @Schema(description = "Case folder identifier", example = "67890", requiredMode = RequiredMode.NOT_REQUIRED) String caseFolderId,

    @Schema(description = "Step type", example = "INITIAL_REVIEW", requiredMode = RequiredMode.NOT_REQUIRED) StepType stepType,

    @Schema(description = "Step status", example = "PENDING", requiredMode = RequiredMode.NOT_REQUIRED) StepStatus status,

    @Schema(description = "Assigned to", example = "user123", requiredMode = RequiredMode.NOT_REQUIRED) String assignedTo,

    @Schema(description = "Owner", example = "owner123", requiredMode = RequiredMode.NOT_REQUIRED) String owner) {

}
