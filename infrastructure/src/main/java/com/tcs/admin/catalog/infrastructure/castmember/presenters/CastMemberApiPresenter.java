package com.tcs.admin.catalog.infrastructure.castmember.presenters;

import com.tcs.admin.catalog.application.castmember.retrieve.get.CastMemberOutput;
import com.tcs.admin.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import com.tcs.admin.catalog.infrastructure.castmember.models.CastMemberListResponse;
import com.tcs.admin.catalog.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberApiPresenter {

    static CastMemberResponse present(final CastMemberOutput output) {
        return new CastMemberResponse(
                output.id(),
                output.name(),
                output.type().name(),
                output.createdAt().toString(),
                output.updatedAt().toString()
        );
    }

    static CastMemberListResponse present(final CastMemberListOutput output) {
        return new CastMemberListResponse(
                output.id(),
                output.name(),
                output.type().name(),
                output.createdAt().toString()
        );
    }
}
