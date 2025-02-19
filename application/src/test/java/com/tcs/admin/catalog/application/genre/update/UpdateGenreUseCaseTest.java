package com.tcs.admin.catalog.application.genre.update;

import com.tcs.admin.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import com.tcs.admin.catalog.application.category.update.UpdateCategoryCommand;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateGenreUseCaseTest {

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(genreGateway, categoryGateway);
    }

    @Test
    public void givenValidCommand_whenCallsUpdateGenre_thenReturnGenreId() {
        final var aGenre = Genre.newGenre("Movi", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        when(genreGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(expectedId);
        Mockito.verify(genreGateway, times(1)).update(argThat(anUpdatedGenre ->
                Objects.equals(expectedName, anUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, anUpdatedGenre.isActive())
                        && Objects.equals(expectedId, anUpdatedGenre.getId())
                        && Objects.equals(expectedCategories, anUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), anUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(anUpdatedGenre.getUpdatedAt())
                        && Objects.isNull(anUpdatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenValidCommandWithCategories_whenCallsUpdateGenre_thenReturnGenreId() {
        final var aGenre = Genre.newGenre("Movi", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        when(genreGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(expectedCategories);

        when(genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, times(1)).existsByIds(expectedCategories);
        Mockito.verify(genreGateway, times(1)).update(argThat(anUpdatedGenre ->
                Objects.equals(expectedName, anUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, anUpdatedGenre.isActive())
                        && Objects.equals(expectedId, anUpdatedGenre.getId())
                        && Objects.equals(expectedCategories, anUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), anUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(anUpdatedGenre.getUpdatedAt())
                        && Objects.isNull(anUpdatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenValidCommandWithInactiveGenre_whenCallsUpdateGenre_thenReturnGenreId() {
        final var aGenre = Genre.newGenre("Movi", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Movies";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        when(genreGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertNull(aGenre.getDeletedAt());
        Assertions.assertTrue(aGenre.isActive());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(expectedId);
        Mockito.verify(genreGateway, times(1)).update(argThat(anUpdatedGenre ->
                Objects.equals(expectedName, anUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, anUpdatedGenre.isActive())
                        && Objects.equals(expectedId, anUpdatedGenre.getId())
                        && Objects.equals(expectedCategories, anUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), anUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(anUpdatedGenre.getUpdatedAt())
                        && Objects.nonNull(anUpdatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdateGenre_thenReturnNotificationException() {
        final var aGenre = Genre.newGenre("Movi", true);
        final var expectedId = aGenre.getId();
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        when(genreGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        final var actualException = Assertions.assertThrows(NotificationException.class, () ->
                useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(genreGateway, times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, times(0)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).update(any());
    }

    @Test
    public void givenInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoNotExist_thenReturnNotificationException() {
        final var aGenre = Genre.newGenre("Movi", true);
        final var expectedId = aGenre.getId();
        final var expectedName = " ";
        final var expectedIsActive = true;

        final var cat1 = CategoryID.from("123");
        final var cat2 = CategoryID.from("456");
        final var cat3 = CategoryID.from("789");
        final var expectedCategories = List.<CategoryID>of(cat1, cat2, cat3);
        final var expectedErrorCount = 2;
        final var expectedErrorMessage1 = "Some categories could not be found: 456, 789";
        final var expectedErrorMessage2 = "'name' should not be empty";

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        when(genreGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(List.of(cat1));

        final var actualException = Assertions.assertThrows(NotificationException.class, () ->
                useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage1, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).message());

        Mockito.verify(genreGateway, times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, times(1)).existsByIds(expectedCategories);
        Mockito.verify(genreGateway, times(0)).update(any());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
