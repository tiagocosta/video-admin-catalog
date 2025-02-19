package com.tcs.admin.catalog.application.genre.update;

import com.tcs.admin.catalog.application.category.update.UpdateCategoryCommand;
import com.tcs.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.tcs.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.tcs.admin.catalog.domain.Identifier;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.genre.GenreID;
import com.tcs.admin.catalog.domain.validation.Error;
import com.tcs.admin.catalog.domain.validation.ValidationHandler;
import com.tcs.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final GenreGateway genreGateway;

    private final CategoryGateway categoryGateway;

    public DefaultUpdateGenreUseCase(
            final GenreGateway genreGateway,
            final CategoryGateway categoryGateway
    ) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand aCommand) {
        final var anId = GenreID.from(aCommand.id());
        final var categories = toCategoryID(aCommand.categories());

        final var aGenre = this.genreGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(() -> aGenre.update(aCommand.name(), aCommand.isActive(), categories));

        if (notification.hasErrors()) {
            throw new NotificationException(
                    "Could not update genre aggregate %s".formatted(aCommand.id()), notification);
        }

        return UpdateGenreOutput.from(genreGateway.update(aGenre));
    }

    private static Supplier<NotFoundException> notFound(Identifier anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }

    private List<CategoryID> toCategoryID(final List<String> categories) {
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = categoryGateway.existsByIds(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(
                    new Error("Some categories could not be found: %s".formatted(missingIdsMessage))
            );
        }

        return notification;
    }
}
