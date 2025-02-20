package com.tcs.admin.catalog.infrastructure.category.presenters;

import com.tcs.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.tcs.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.tcs.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.tcs.admin.catalog.infrastructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {

    static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
                output.id(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
