package com.tcs.admin.catalog.application.category.retrieve.get;

import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.DomainException;
import com.tcs.admin.catalog.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String anIn) {
        final var anId  = CategoryID.from(anIn);
        return this.categoryGateway.findById(anId)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(anId));
    }

    private static Supplier<DomainException> notFound(CategoryID anId) {
        return () -> DomainException.with(
                new Error("Category with ID %s not found".formatted(anId.getValue()))
        );
    }
}
