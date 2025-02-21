package com.tcs.admin.catalog.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record GenreResponse(
        String id,
        String name,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("categories_id") List<String> categories,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public boolean isActive() {
        return active != null ? active : true;
    }
}
