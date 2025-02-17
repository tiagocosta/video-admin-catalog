package com.tcs.admin.catalog.application.category.retrieve.get;

import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenValidId_whenCallGetCategory_thenReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        final var actualGetCategoryOutput = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, actualGetCategoryOutput.id());
        Assertions.assertEquals(expectedName, actualGetCategoryOutput.name());
        Assertions.assertEquals(expectedDescription, actualGetCategoryOutput.description());
        Assertions.assertEquals(expectedIsActive, actualGetCategoryOutput.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualGetCategoryOutput.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualGetCategoryOutput.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualGetCategoryOutput.deletedAt());
    }

    @Test
    public void givenInvalidId_whenCallGetCategory_thenReturnNotFound() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenValidId_whenGatewayThrowsException_thenReturnException() {
        final var expectedErrorMessage = "gateway error";
        final var aCategory =
            Category.newCategory("Movies", "Most watched", true);

        final var expectedId = aCategory.getId();

        doThrow(new IllegalStateException("gateway error"))
                .when(categoryGateway).findById(eq(expectedId));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));
    }
}
