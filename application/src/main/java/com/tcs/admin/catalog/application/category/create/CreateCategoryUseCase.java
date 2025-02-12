package com.tcs.admin.catalog.application.category.create;

import com.tcs.admin.catalog.application.UseCase;
import com.tcs.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
