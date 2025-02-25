package com.tcs.admin.catalog.infrastructure.castmember.models;

import java.time.Instant;

public record CastMemberResponse(
        String id,
        String name,
        String type,
        String createdAt,
        String updatedAt
) {
}
