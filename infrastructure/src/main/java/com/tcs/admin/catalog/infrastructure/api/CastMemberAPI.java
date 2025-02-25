package com.tcs.admin.catalog.infrastructure.api;

import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.infrastructure.castmember.models.CastMemberListResponse;
import com.tcs.admin.catalog.infrastructure.castmember.models.CastMemberResponse;
import com.tcs.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.tcs.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/cast_members")
@Tag(name = "Cast Members")
public interface CastMemberAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new cas member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    ResponseEntity<?> create(@RequestBody CreateCastMemberRequest input);

    @GetMapping
    @Operation(summary = "List all cast members - filtered and paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid parameter received"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    Pagination<CastMemberListResponse> list(
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
    @Operation(summary = "Get a cast member by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cast member retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cast member not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    CastMemberResponse getById(@PathVariable String id);

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a cast member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    ResponseEntity<?> update(
            @PathVariable  String id,
            @RequestBody UpdateCastMemberRequest input
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a cast member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    void deleteById(@PathVariable  String id);
}
