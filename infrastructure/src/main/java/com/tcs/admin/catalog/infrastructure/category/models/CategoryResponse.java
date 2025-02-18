package com.tcs.admin.catalog.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CategoryResponse(
        String id,
        String name,
        String description,
        @JsonProperty("is_active") Boolean active,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
}
