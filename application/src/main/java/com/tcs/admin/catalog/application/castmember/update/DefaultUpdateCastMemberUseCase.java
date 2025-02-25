package com.tcs.admin.catalog.application.castmember.update;

import com.tcs.admin.catalog.domain.Identifier;
import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public non-sealed class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultUpdateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberCommand aCommand) {
        final var anId = CastMemberID.from(aCommand.id());

        final var aCastMember = this.castMemberGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.validate(() -> aCastMember.update(aCommand.name(), aCommand.type()));

        if (notification.hasErrors()) {
            notify(anId, notification);
        }

        return UpdateCastMemberOutput.from(castMemberGateway.update(aCastMember));
    }

    private static void notify(final Identifier anId, Notification notification) {
        throw new NotificationException(
                "Could not update cast member aggregate with ID %s".formatted(anId.getValue()),
                notification
        );
    }

    private static Supplier<NotFoundException> notFound(Identifier anId) {
        return () -> NotFoundException.with(CastMember.class, anId);
    }
}
