package com.tcs.admin.catalog.domain.category;

import com.tcs.admin.catalog.domain.AggregateRoot;
import com.tcs.admin.catalog.domain.utils.InstantUtils;
import com.tcs.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant anUpdateDate,
            final Instant aDeletionDate
    ) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(anUpdateDate, "'updatedAt' should not be null");
        this.deletedAt = aDeletionDate;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean isActive) {
        final var id = CategoryID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, aName, aDescription, isActive, now, now, deletedAt);
    }

    public static Category with(final Category aCategory) {
        return with(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt()
        );
    }

    public static Category with(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant anUpdateDate,
            final Instant aDeletionDate
    ) {
        return new Category(
                anId,
                aName,
                aDescription,
                isActive,
                aCreationDate,
                anUpdateDate,
                aDeletionDate
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category activate() {
        this.deletedAt = null;
        this.updatedAt = InstantUtils.now();
        this.active = true;

        return this;
    }

    public Category deactivate() {
        if (this.deletedAt == null) {
            this.deletedAt = InstantUtils.now();
            this.updatedAt = this.deletedAt;
            this.active = false;
        }

        return this;
    }

    public Category update(
            final String aName,
            final String aDescription,
            final boolean isAcitve
    ) {
        if (isAcitve) {
            activate();
        } else {
            deactivate();
        }
        this.name = aName;
        this.description = aDescription;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
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

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
