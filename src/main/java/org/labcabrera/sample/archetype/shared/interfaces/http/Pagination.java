package org.labcabrera.sample.archetype.shared.interfaces.http;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Pagination information")
public record Pagination(

    @Schema(description = "Current page number (0-based)", example = "0") int page,

    @Schema(description = "Number of items per page", example = "20") int size,

    @Schema(description = "Total number of elements", example = "100") long totalElements,

    @Schema(description = "Total number of pages", example = "5") int totalPages

) {
}
