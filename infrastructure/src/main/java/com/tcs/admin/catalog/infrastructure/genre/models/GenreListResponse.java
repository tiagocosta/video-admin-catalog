package com.tcs.admin.catalog.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record GenreListResponse(
        String id,
        String name,
        @JsonProperty("is_active") Boolean active,
        Instant createdAt,
        Instant deletedAt
) {
}
