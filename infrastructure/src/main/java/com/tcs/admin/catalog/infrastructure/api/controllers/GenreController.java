package com.tcs.admin.catalog.infrastructure.api.controllers;

import com.tcs.admin.catalog.application.genre.create.CreateGenreCommand;
import com.tcs.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.tcs.admin.catalog.application.genre.delete.DeleteGenreUseCase;
import com.tcs.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.tcs.admin.catalog.application.genre.retrieve.list.ListGenresUseCase;
import com.tcs.admin.catalog.application.genre.update.UpdateGenreCommand;
import com.tcs.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.pagination.SearchQuery;
import com.tcs.admin.catalog.infrastructure.api.GenreAPI;
import com.tcs.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.tcs.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.tcs.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.tcs.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import com.tcs.admin.catalog.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final ListGenresUseCase listGenresUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final GetGenreByIdUseCase getGenreByIdUseCase,
            final UpdateGenreUseCase updateGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase,
            final ListGenresUseCase listGenresUseCase
    ) {
        this.createGenreUseCase = Objects.requireNonNull(createGenreUseCase);
        this.getGenreByIdUseCase = Objects.requireNonNull(getGenreByIdUseCase);
        this.updateGenreUseCase = Objects.requireNonNull(updateGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
        this.listGenresUseCase = Objects.requireNonNull(listGenresUseCase);
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
        return this.listGenresUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(final String id) {
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateGenreRequest input) {
        final var aCommand =
                UpdateGenreCommand.with(id, input.name(), input.active(), input.categories());
        final var output = updateGenreUseCase.execute(aCommand);
        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteGenreUseCase.execute(id);
    }
}

