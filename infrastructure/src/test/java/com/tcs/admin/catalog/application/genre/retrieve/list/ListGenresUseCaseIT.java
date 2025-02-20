package com.tcs.admin.catalog.application.genre.retrieve.list;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.pagination.SearchQuery;
import com.tcs.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.tcs.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

@IntegrationTest
public class ListGenresUseCaseIT {

    @Autowired
    private ListGenresUseCase useCase;

    @Autowired
    private GenreRepository genreRepository;

    @MockitoSpyBean
    private GenreGateway genreGateway;

    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        this.genreRepository.deleteAll();
    }

    @Test
    public void givenValidQuery_whenCallListGenres_thenReturnGenres() {
        final var genres = List.of(
                Genre.newGenre("Action", true),
                Genre.newGenre("Fiction", true)
        );

        genreRepository.saveAllAndFlush(genres.stream()
                .map(GenreJpaEntity::from)
                .toList()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 2;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedItems = genres.stream()
                .map(GenreListOutput::from)
                .toList();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertTrue(
                expectedItems.size() == actualResult.items().size()
                        && expectedItems.containsAll(actualResult.items())
        );
    }

    @Test
    public void givenValidQuery_whenHasNoGenres_thenReturnEmptyGenres() {
        final var genres = List.<Genre>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 0;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }
}
