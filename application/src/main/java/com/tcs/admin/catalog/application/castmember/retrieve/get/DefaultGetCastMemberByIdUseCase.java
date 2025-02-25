package com.tcs.admin.catalog.application.castmember.retrieve.get;

import com.tcs.admin.catalog.domain.Identifier;
import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public non-sealed class DefaultGetCastMemberByIdUseCase extends GetCastMemberByIdUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultGetCastMemberByIdUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMemberOutput execute(final String anIn) {
        final var anId = CastMemberID.from(anIn);
        return this.castMemberGateway.findById(anId)
                .map(CastMemberOutput::from)
                .orElseThrow(notFound(anId));
    }

    private static Supplier<NotFoundException> notFound(Identifier anId) {
        return () -> NotFoundException.with(CastMember.class, anId);
    }
}
