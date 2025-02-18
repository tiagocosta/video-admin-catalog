package com.tcs.admin.catalog.domain.genre;

import com.tcs.admin.catalog.domain.exceptions.DomainException;
import com.tcs.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
