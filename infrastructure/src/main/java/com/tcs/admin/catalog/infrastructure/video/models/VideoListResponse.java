package com.tcs.admin.catalog.infrastructure.video.models;

import java.time.Instant;

public record VideoListResponse(
        String id,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}
