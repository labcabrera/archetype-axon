package org.labcabrera.sample.archetype.casefolder.interfaces.http.dto;

import org.labcabrera.sample.archetype.casefolder.domain.valueobjects.IdCardType;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public record IdCardDto(

    @Schema(description = "Id card type", example = "NIF", requiredMode = RequiredMode.REQUIRED) IdCardType type,

    @Schema(description = "Id card number", example = "12345678A", requiredMode = RequiredMode.REQUIRED) String number

) {

}
