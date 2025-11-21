package org.labcabrera.sample.archetype.casefolder.interfaces.http.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data to create a new case folder")
public record CreateCaseFolderRequest(

    @Schema(description = "Name", example = "John", requiredMode = RequiredMode.REQUIRED) @NotNull String name,

    @Schema(description = "First surname", example = "Doe", requiredMode = RequiredMode.REQUIRED) @NotNull String firstSurname,

    @Schema(description = "Last surname", example = "Smith", requiredMode = RequiredMode.NOT_REQUIRED) String lastSurname,

    @Schema(description = "IdCard", requiredMode = RequiredMode.REQUIRED) @NotNull IdCardDto idCard) {
}
