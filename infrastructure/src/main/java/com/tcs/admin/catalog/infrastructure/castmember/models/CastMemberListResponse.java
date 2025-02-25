package com.tcs.admin.catalog.infrastructure.castmember.models;

import com.tcs.admin.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberListResponse(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt
) {
}
