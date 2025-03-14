package com.tcs.admin.catalog.application.genre.delete;

import com.tcs.admin.catalog.application.UseCaseTest;
import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteGenreUseCaseTest extends UseCaseTest {

    @Mock
    private GenreGateway genreGateway;

    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Override
    protected void cleanUp() {
        Mockito.reset(genreGateway);
    }

    @Test
    public void givenValidId_whenCallsDeleteGenre_thenOk() {
        final var aGenre =
                Genre.newGenre("Movies", true);

        final var expectedId = aGenre.getId();

        doNothing()
                .when(genreGateway).deleteById(any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenInvalidId_whenCallsDeleteGenre_thenOk() {
        final var expectedId = GenreID.from("123");

        doNothing()
                .when(genreGateway).deleteById(any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenValidId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_thenReceiveException() {
        final var expectedId = GenreID.from("123");

        doThrow(new IllegalStateException("gateway error"))
                .when(genreGateway).deleteById(any());

        final var actualException
                = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals("gateway error", actualException.getMessage());

        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);
    }

}
