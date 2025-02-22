package com.tcs.admin.catalog.application.castmember.retrieve.get;

import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {

    public static CastMemberOutput from(final CastMember aCastMember) {
       return new CastMemberOutput(
               aCastMember.getId().getValue(),
               aCastMember.getName(),
               aCastMember.getType(),
               aCastMember.getCreatedAt(),
               aCastMember.getUpdatedAt()
       );
    }
}
