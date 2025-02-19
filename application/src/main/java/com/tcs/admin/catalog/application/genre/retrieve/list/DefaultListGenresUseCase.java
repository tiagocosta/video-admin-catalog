package com.tcs.admin.catalog.application.genre.retrieve.list;

import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListGenresUseCase extends ListGenresUseCase {

    private final GenreGateway genreGateway;

    public DefaultListGenresUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<GenreListOutput> execute(SearchQuery aQuery) {
        return this.genreGateway.findAll(aQuery)
                .map(GenreListOutput::from);
    }
}
