package com.tcs.admin.catalog.domain.genre;

import com.tcs.admin.catalog.domain.AggregateRoot;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.category.CategoryValidator;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.domain.utils.InstantUtils;
import com.tcs.admin.catalog.domain.validation.ValidationHandler;
import com.tcs.admin.catalog.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreID> {

    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Genre(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreationDate,
            final Instant anUpdateDate,
            final Instant aDeletionDate
    ) {
        super(anId);
        this.name = aName;
        this.active = isActive;
        this.categories = categories;
        this.createdAt = aCreationDate;
        this.updatedAt = anUpdateDate;
        this.deletedAt = aDeletionDate;
        selfValidate();
    }

    public static Genre newGenre(final String aName, final boolean isActive) {
        final var anId = GenreID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Genre(
                anId,
                aName,
                isActive,
                new ArrayList<>(),
                now,
                now,
                deletedAt
        );
    }

    public static Genre with(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreationDate,
            final Instant anUpdateDate,
            final Instant aDeletionDate
    ) {
        return new Genre(
                anId,
                aName,
                isActive,
                categories,
                aCreationDate,
                anUpdateDate,
                aDeletionDate
        );
    }

    public static Genre with(final Genre aGenre) {
        return new Genre(
                aGenre.id,
                aGenre.name,
                aGenre.active,
                new ArrayList<>(aGenre.categories),
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public Genre activate() {
        this.deletedAt = null;
        this.updatedAt = InstantUtils.now();
        this.active = true;

        return this;
    }

    public Genre deactivate() {
        if (this.deletedAt == null) {
            this.deletedAt = InstantUtils.now();
            this.updatedAt = this.deletedAt;
            this.active = false;
        }

        return this;
    }

    public Genre update(
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories
    ) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.name = aName;
        this.updatedAt = InstantUtils.now();
        this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
        selfValidate();
        return this;
    }

    public Genre addCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null) {
            return this;
        }
        this.categories.add(aCategoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre addCategories(final List<CategoryID> categories) {
        if (categories == null || categories.isEmpty()) {
            return this;
        }
        this.categories.addAll(categories);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre removeCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null) {
            return this;
        }
        this.categories.remove(aCategoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasErrors()) {
            throw new NotificationException("Failed to create aggregate Genre", notification);
        }
    }
}
