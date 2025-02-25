package com.tcs.admin.catalog.application.castmember.create;

import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.domain.validation.handler.Notification;

import java.util.Objects;

public non-sealed class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultCreateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand aCommand) {
        final var notification = Notification.create();
        final var aCastMember = notification.validate(
                () -> CastMember.newMember(aCommand.name(), aCommand.type())
        );

        if (notification.hasErrors()) {
            notify(notification);
        }

        return CreateCastMemberOutput.from(this.castMemberGateway.create(aCastMember));
    }

    private static void notify(Notification notification) {
        throw new NotificationException("Could not create cast member aggregate", notification);
    }
}
