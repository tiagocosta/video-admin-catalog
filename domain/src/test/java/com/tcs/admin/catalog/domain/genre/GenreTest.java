package com.tcs.admin.catalog.domain.genre;

import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.DomainException;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallNewGenre_thenInstantiateGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategoriesCount = 0;

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategoriesCount, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallNewGenreAndValidate_thenReceiveError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Genre.newGenre(expectedName, expectedIsActive)
        );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallNewGenreAndValidate_thenReceiveError() {
        final String expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Genre.newGenre(expectedName, expectedIsActive)
        );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_thenReceiveError() {
        final String expectedName = """
                There are many variations of passages of Lorem Ipsum available, but the majority have suffered
                 alteration in some form, by injected humour, or randomised words which don't look even slightly
                 believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything
                 embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to
                 repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a
                 dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate
                 Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition,
                 injected humour, or non-characteristic words etc.
                """;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Genre.newGenre(expectedName, expectedIsActive)
        );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenActiveGenre_whenCallDeactivate_thenReceiveOk() {
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategoriesCount = 0;

        final var actualGenre = Genre.newGenre(expectedName, true);

        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        actualGenre.deactivate();

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategoriesCount, actualGenre.getCategories().size());
        Assertions.assertEquals(createdAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(updatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenInactiveGenre_whenCallActivate_thenReceiveOk() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategoriesCount = 0;

        final var actualGenre = Genre.newGenre(expectedName, false);

        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        actualGenre.activate();

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategoriesCount, actualGenre.getCategories().size());
        Assertions.assertEquals(createdAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(updatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidInactiveGenre_whenCallUpdate_thenReturnUpdatedGenre() {
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("Movi", expectedIsActive);

        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        Assertions.assertDoesNotThrow(() -> actualGenre.update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(createdAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidGenre_whenCallUpdateWithInvalidParams_thenReturnReceiveNotificationException() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre= Genre.newGenre("Movi", expectedIsActive);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions.assertThrows(NotificationException.class, () ->
                actualGenre.update(expectedName, expectedIsActive, expectedCategories)
        );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenValidGenre_whenCallUpdateWithNullCategories_thenReturnUpdatedGenre() {
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        Assertions.assertDoesNotThrow(() -> actualGenre.update(expectedName, expectedIsActive, null));

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(createdAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidGenreWithNoCategory_whenCallAddCategory_thenReturnOk() {
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var seriesId = CategoryID.from("123");
        final var moviesId = CategoryID.from("456");
        final var expectedCategories = List.of(seriesId, moviesId);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(seriesId);
        actualGenre.addCategory(moviesId);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(createdAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidGenreWithTwoCategories_whenCallRemoveCategory_thenReturnOk() {
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var seriesId = CategoryID.from("123");
        final var moviesId = CategoryID.from("456");
        final var expectedCategories = List.of(moviesId);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, List.of(seriesId, moviesId));

        Assertions.assertEquals(2, actualGenre.getCategories().size());

        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(seriesId);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(createdAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidNullCategoryID_whenCallAddCategory_thenReturnOk() {
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(null);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(createdAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(updatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidNullCategoryID_whenCallRemoveCategory_thenReturnOk() {
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var seriesId = CategoryID.from("123");
        final var moviesId = CategoryID.from("456");
        final var expectedCategories = List.of(seriesId, moviesId);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, List.of(seriesId, moviesId));

        Assertions.assertEquals(2, actualGenre.getCategories().size());

        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(null);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(createdAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(updatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }
}
