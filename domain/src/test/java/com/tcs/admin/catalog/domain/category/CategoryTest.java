package com.tcs.admin.catalog.domain.category;

import com.tcs.admin.catalog.domain.exceptions.DomainException;
import com.tcs.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void givenValidParams_whenCallNewCategory_thenInstantiateCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenNullName_whenCallNewCategory_thenReceiveError() {
        final String expectedName = null;
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException =
                Assertions.assertThrows(
                        DomainException.class,
                        () -> actualCategory.validate(new ThrowsValidationHandler())
                );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

}
