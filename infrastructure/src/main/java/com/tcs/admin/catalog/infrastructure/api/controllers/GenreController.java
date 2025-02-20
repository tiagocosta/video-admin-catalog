package com.tcs.admin.catalog.infrastructure.api.controllers;

import com.tcs.admin.catalog.application.genre.create.CreateGenreCommand;
import com.tcs.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.infrastructure.api.GenreAPI;
import com.tcs.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.tcs.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.tcs.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.tcs.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase
    ) {
        this.createGenreUseCase = Objects.requireNonNull(createGenreUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var aCommand =
                CreateGenreCommand.with(input.name(), input.active(), input.categories());
        final var output = createGenreUseCase.execute(aCommand);
        return ResponseEntity.created(URI.create("/genres/" + output.id()))
                .body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return null;
    }

    @Override
    public GenreResponse getById(final String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateGenreRequest input) {
        return null;
    }

    @Override
    public void deleteById(final String id) {

    }
}

