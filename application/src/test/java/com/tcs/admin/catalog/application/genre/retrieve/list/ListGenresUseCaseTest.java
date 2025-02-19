package com.tcs.admin.catalog.application.genre.retrieve.list;

import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListGenresUseCaseTest {

    @Mock
    private GenreGateway genreGateway;

    @InjectMocks
    private DefaultListGenresUseCase useCase;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(genreGateway);
    }

    @Test
    public void givenValidQuery_whenCallListGenres_thenReturnGenres() {
        final var genres = List.of(
                Genre.newGenre("Action", true),
                Genre.newGenre("Fiction", true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedItems = genres.stream().map(GenreListOutput::from).toList();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination =
                new Pagination<Genre>(expectedPage, expectedPerPage, genres.size(), genres);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(GenreListOutput::from);

        when(genreGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedItemsCount, actualResult.total());
        Assertions.assertEquals(expectedItems, actualResult.items());

        Mockito.verify(genreGateway, times(1)).findAll(aQuery);
    }

    @Test
    public void givenValidQuery_whenHasNoGenres_thenReturnEmptyGenres() {
        final var genres = List.<Genre>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination =
                new Pagination<Genre>(expectedPage, expectedPerPage, genres.size(), genres);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(GenreListOutput::from);

        when(genreGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedItemsCount, actualResult.total());
    }

    @Test
    public void givenValidQuery_whenGatewayThrowsException_thenReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "gateway error";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(genreGateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(aQuery)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
