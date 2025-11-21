package org.labcabrera.sample.archetype.casefolder.interfaces.http.dto;

import org.labcabrera.sample.archetype.casefolder.domain.valueobjects.CaseFolderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Case folder information")
public record CaseFolderDto(

    @Schema(description = "Unique identifier of the case folder", example = "550e8400-e29b-41d4-a716-446655440000") String id,

    @Schema(description = "Status", example = "ACTIVE", requiredMode = RequiredMode.REQUIRED) CaseFolderStatus status,

    @Schema(description = "Name", example = "John", requiredMode = RequiredMode.REQUIRED) String name,

    @Schema(description = "First surname", example = "Doe", requiredMode = RequiredMode.REQUIRED) String firstSurname,

    @Schema(description = "Last surname", example = "Smith", requiredMode = RequiredMode.NOT_REQUIRED) String lastSurname,

    @Schema(description = "IdCard", requiredMode = RequiredMode.REQUIRED) IdCardDto idCard,

    @Schema(description = "Entity owner", example = "john.doe", requiredMode = RequiredMode.REQUIRED) String owner

) {
}
