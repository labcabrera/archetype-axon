package org.labcabrera.sample.archetype.shared.interfaces.http;

import java.util.List;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Paginated response")
public record PageResponse<T>(

    @Schema(description = "List of items in the current page") List<T> content,

    @Schema(description = "Pagination details") Pagination pagination

) {
    public PageResponse(Page<T> page) {
        this(
            page.getContent(),
            new Pagination(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()));
    }
}
