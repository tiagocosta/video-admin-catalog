package com.tcs.admin.catalog.domain.castmember;

import com.tcs.admin.catalog.domain.AggregateRoot;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.domain.utils.InstantUtils;
import com.tcs.admin.catalog.domain.validation.ValidationHandler;
import com.tcs.admin.catalog.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.Objects;

public class CastMember extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    private CastMember(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant anUpdateDate
    ) {
        super(anId);
        this.name = aName;
        this.type = aType;
        this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(anUpdateDate, "'updatedAt' should not be null");
        selfValidate();
    }

    public static CastMember newMember(final String aName, final CastMemberType aType) {
        final var id = CastMemberID.unique();
        final var now = InstantUtils.now();
        return new CastMember(id, aName, aType, now, now);
    }

    public static CastMember with(final CastMember aCastMember) {
        return with(
                aCastMember.getId(),
                aCastMember.getName(),
                aCastMember.getType(),
                aCastMember.getCreatedAt(),
                aCastMember.getUpdatedAt()
        );
    }

    public static CastMember with(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant anUpdateDate
    ) {
        return new CastMember(
                anId,
                aName,
                aType,
                aCreationDate,
                anUpdateDate
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CastMemberValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasErrors()) {
            throw new NotificationException("Failed to create aggregate CastMember", notification);
        }
    }
}
