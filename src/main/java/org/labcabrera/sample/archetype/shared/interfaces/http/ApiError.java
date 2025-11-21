package org.labcabrera.sample.archetype.shared.interfaces.http;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API error details")
public record ApiError(

    @Schema(description = "Error code") String code,

    @Schema(description = "Detailed error message") String message,

    @Schema(description = "Timestamp of the error") LocalDateTime timestamp,

    @Schema(description = "Validation errors, if any") List<ApiErrorDetail> details) {
}
