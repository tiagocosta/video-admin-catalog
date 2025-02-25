package com.tcs.admin.catalog.infrastructure.castmember.models;

import com.tcs.admin.catalog.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(
        String name,
        CastMemberType type
) {
}
