package com.tcs.admin.catalog.application.category.retrieve.get;

import com.tcs.admin.catalog.domain.category.Category;

import java.time.Instant;

public record CategoryOutput(
        String id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static CategoryOutput from(final Category aCategory) {
       return new CategoryOutput(
               aCategory.getId().getValue(),
               aCategory.getName(),
               aCategory.getDescription(),
               aCategory.isActive(),
               aCategory.getCreatedAt(),
               aCategory.getUpdatedAt(),
               aCategory.getDeletedAt()
       );
    }
}
