package com.tcs.admin.catalog.application.genre.create;

import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.genre.Genre;

public record CreateGenreOutput(
        String id
) {

    public static CreateGenreOutput from(final String id) {
        return new CreateGenreOutput(id);
    }

    public static CreateGenreOutput from(final Genre aGenre) {
        return new CreateGenreOutput(aGenre.getId().getValue());
    }
}
