package org.labcabrera.sample.archetype.casefolder.interfaces.http.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Data to update an existing case folder")
public record UpdateCaseFolderRequest(

    @Schema(description = "Name", example = "John", requiredMode = RequiredMode.NOT_REQUIRED) String name,

    @Schema(description = "First surname", example = "Doe", requiredMode = RequiredMode.NOT_REQUIRED) String firstSurname,

    @Schema(description = "Last surname", example = "Smith", requiredMode = RequiredMode.NOT_REQUIRED) String lastSurname) {
}
