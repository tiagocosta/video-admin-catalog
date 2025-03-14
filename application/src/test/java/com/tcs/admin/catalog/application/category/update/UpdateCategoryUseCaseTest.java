package com.tcs.admin.catalog.application.category.update;

import com.tcs.admin.catalog.application.UseCaseTest;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest extends UseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Override
    protected void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCategory_thenReturnCategoryId() {
        final var aCategory = Category.newCategory("Movi", null, true);
        final var expectedId = aCategory.getId();
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, times(1)).update(argThat(anUpdatedCategory ->
                Objects.equals(expectedName, anUpdatedCategory.getName())
                        && Objects.equals(expectedDescription, anUpdatedCategory.getDescription())
                        && Objects.equals(expectedIsActive, anUpdatedCategory.isActive())
                        && Objects.equals(expectedId, anUpdatedCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), anUpdatedCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(anUpdatedCategory.getUpdatedAt())
                        && Objects.isNull(anUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdateCategory_thenReturnDomainException() {
        final var aCategory = Category.newCategory("Movies", null, true);
        final var expectedId = aCategory.getId();
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var expectedErrorMessage  = "'name' should not be null";
        final var expectedErrorCount  = 1;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                null,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(0)).update(any());
    }

    @Test
    public void givenValidDeactivateCommand_whenCallsUpdateCategory_thenReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory("Movi", "Most w", true);
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, times(1)).update(argThat(anUpdatedCategory ->
                Objects.equals(expectedName, anUpdatedCategory.getName())
                        && Objects.equals(expectedDescription, anUpdatedCategory.getDescription())
                        && Objects.equals(expectedIsActive, anUpdatedCategory.isActive())
                        && Objects.equals(expectedId, anUpdatedCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), anUpdatedCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(anUpdatedCategory.getUpdatedAt())
                        && Objects.nonNull(anUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenValidCommand_whenGatewayThrowsRandomException_thenReturnException() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "gateway error";

        final var aCategory = Category.newCategory("Movi", "Most w", true);
        final var expectedId = aCategory.getId();

        final var aCommand =
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory));

        when(categoryGateway.update(any()))
                .thenThrow(new IllegalStateException("gateway error"));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(1)).update(argThat(anUpdatedCategory ->
                Objects.equals(expectedName, anUpdatedCategory.getName())
                        && Objects.equals(expectedDescription, anUpdatedCategory.getDescription())
                        && Objects.equals(expectedIsActive, anUpdatedCategory.isActive())
                        && Objects.equals(expectedId, anUpdatedCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), anUpdatedCategory.getCreatedAt())
                        && Objects.equals(aCategory.getUpdatedAt(), anUpdatedCategory.getUpdatedAt())
                        && Objects.isNull(anUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenValidCommandWithInvalidId_whenCallsUpdateCategory_thenReturnNotFoundException() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId);

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualException =
                Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, times(1)).findById(CategoryID.from(expectedId));
        Mockito.verify(categoryGateway, times(0)).update(any());
    }
}
