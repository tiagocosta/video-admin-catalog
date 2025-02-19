package com.tcs.admin.catalog.application.genre.create;

import com.tcs.admin.catalog.application.UseCase;
import com.tcs.admin.catalog.application.category.create.CreateCategoryCommand;
import com.tcs.admin.catalog.application.category.create.CreateCategoryOutput;
import com.tcs.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateGenreUseCase
        extends UseCase<CreateGenreCommand, CreateGenreOutput> {
}
