package com.tcs.admin.catalog.application.genre.retrieve.get;

import com.tcs.admin.catalog.domain.Identifier;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.genre.GenreID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase {

    private final GenreGateway genreGateway;

    public DefaultGetGenreByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(final String anIn) {
        final var anId  = GenreID.from(anIn);
        return this.genreGateway.findById(anId)
                .map(GenreOutput::from)
                .orElseThrow(notFound(anId));
    }

    private static Supplier<NotFoundException> notFound(Identifier anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }
}
