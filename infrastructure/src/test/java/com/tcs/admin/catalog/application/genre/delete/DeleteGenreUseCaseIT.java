package com.tcs.admin.catalog.application.genre.delete;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.genre.GenreID;
import com.tcs.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.tcs.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;

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
    public void givenValidId_whenCallsDeleteGenre_thenOk() {
        final var aGenre =
                Genre.newGenre("Movies", true);

        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, genreRepository.count());

        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenInvalidId_whenCallsDeleteGenre_thenOk() {
        final var expectedId = GenreID.from("123");

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
