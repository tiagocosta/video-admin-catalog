package com.tcs.admin.catalog.infrastructure.api;

import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.tcs.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.tcs.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.tcs.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/genres")
@Tag(name = "Genres")
public interface GenreAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    ResponseEntity<?> create(@RequestBody CreateGenreRequest input);

    @GetMapping
    @Operation(summary = "List all genres - filtered and paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid parameter received"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    Pagination<GenreListResponse> list(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a genre by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Gene not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    GenreResponse getById(@PathVariable String id);

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    ResponseEntity<?> update(
            @PathVariable  String id,
            @RequestBody UpdateGenreRequest input
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    void deleteById(@PathVariable  String id);
}
