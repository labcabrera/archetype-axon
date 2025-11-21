package org.labcabrera.sample.archetype.casefolder.interfaces.http;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Counters", description = "Micrometer Sample Management API")
@RequestMapping("/api/v1/counters")
public interface CounterControllerDefinition {

    @GetMapping
    @Operation(summary = "Get all counters", description = "Returns all counters with current values")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK")
    })
    Map<String, Integer> getCounters();

    @PostMapping("/{key}")
    @Operation(summary = "Increment counter", description = "Increment the counter identified by the given key")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "New counter value"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    Integer incrementCounter(String key);

    @PutMapping("/{key}/{value}")
    @Operation(summary = "Set counter value", description = "Set the counter value for a specific key")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Previous counter value returned"),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    Integer setCounter(String key, Integer value);

}
