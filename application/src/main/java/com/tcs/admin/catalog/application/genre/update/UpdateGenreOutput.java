package com.tcs.admin.catalog.application.genre.update;

import com.tcs.admin.catalog.domain.genre.Genre;

public record UpdateGenreOutput(
        String id
) {

    public static UpdateGenreOutput from(final String anId) {
        return new UpdateGenreOutput(anId);
    }

    public static UpdateGenreOutput from(final Genre aGenre) {
        return new UpdateGenreOutput(aGenre.getId().getValue());
    }
}
