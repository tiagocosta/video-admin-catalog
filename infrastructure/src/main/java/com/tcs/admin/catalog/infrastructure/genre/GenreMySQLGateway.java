package com.tcs.admin.catalog.infrastructure.genre;

import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.genre.GenreID;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.pagination.SearchQuery;
import com.tcs.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.tcs.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    public GenreMySQLGateway(final GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre create(Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public void deleteById(GenreID anId) {
        final var id = anId.getValue();
        if (this.genreRepository.existsById(id)) {
            this.genreRepository.deleteById(id);
        }
    }

    @Override
    public Optional<Genre> findById(GenreID anId) {
        return this.genreRepository.findById(anId.getValue())
                .map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Genre update(Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery aQuery) {
        return null;
    }

    private Genre save(Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.from(aGenre)).toAggregate();
    }
}
