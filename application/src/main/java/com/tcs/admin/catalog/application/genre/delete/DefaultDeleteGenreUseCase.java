package com.tcs.admin.catalog.application.genre.delete;

import com.tcs.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.genre.GenreID;

import java.util.Objects;

public class DefaultDeleteGenreUseCase extends DeleteCategoryUseCase {

    private final GenreGateway genreGateway;

    public DefaultDeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(final String anId) {
        this.genreGateway.deleteById(GenreID.from(anId));
    }
}
