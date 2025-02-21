package com.tcs.admin.catalog.application.castmember.create;

import com.tcs.admin.catalog.domain.castmember.CastMember;

public record CreateCastMemberOutput(
        String id
) {

    public static CreateCastMemberOutput from(final String id) {
        return new CreateCastMemberOutput(id);
    }

    public static CreateCastMemberOutput from(final CastMember aCastMember) {
        return new CreateCastMemberOutput(aCastMember.getId().getValue());
    }
}
