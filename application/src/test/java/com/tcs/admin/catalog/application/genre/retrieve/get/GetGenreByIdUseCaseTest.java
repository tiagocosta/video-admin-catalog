package com.tcs.admin.catalog.application.genre.retrieve.get;

import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetGenreByIdUseCaseTest {

    @Mock
    private GenreGateway genreGateway;

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(genreGateway);
    }

    @Test
    public void givenValidId_whenCallGetGenre_thenReturnGenre() {
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        when(genreGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aGenre));

        final var actualOutput = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedIsActive, actualOutput.isActive());
        Assertions.assertEquals(asString(expectedCategories), actualOutput.categories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualOutput.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualOutput.deletedAt());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallGetGenre_thenReturnNotFound() {
        final var expectedId = GenreID.from("123");
        final var expectedErrorMessage = "Genre with ID %s was not found".formatted(expectedId.getValue());

        when(genreGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenValidId_whenGatewayThrowsException_thenReturnException() {
        final var expectedErrorMessage = "gateway error";
        final var aGenre =
            Genre.newGenre("Movies", true);

        final var expectedId = aGenre.getId();

        doThrow(new IllegalStateException("gateway error"))
                .when(genreGateway).findById(eq(expectedId));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
