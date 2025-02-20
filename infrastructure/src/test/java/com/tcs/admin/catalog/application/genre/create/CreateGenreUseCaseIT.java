package com.tcs.admin.catalog.application.genre.create;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.DomainException;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.genre.GenreID;
import com.tcs.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@IntegrationTest
public class CreateGenreUseCaseIT {

    @Autowired
    private CreateGenreUseCase useCase;

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
    public void givenValidCommandWithCategories_whenCallsCreateGenre_thenReturnGenreId() {
        final var cat1 = categoryGateway.create(
                Category.newCategory("Movies", null, true)
        );

        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(cat1.getId());

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualOutput = useCase.execute(aCommand);

        final var actualEntity
                = genreRepository.findById(GenreID.from(actualOutput.id()).getValue()).get();

        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualEntity.getCategoryIDs().size()
                && expectedCategories.containsAll(actualEntity.getCategoryIDs())
        );
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenValidCommandWithoutCategories_whenCallsCreateGenre_thenReturnGenreId() {
        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualOutput = useCase.execute(aCommand);

        final var actualEntity
                = genreRepository.findById(GenreID.from(actualOutput.id()).getValue()).get();

        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualEntity.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualEntity.getCategoryIDs())
        );
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenValidCommandWithInactiveGenre_whenCallsCreateGenre_thenReturnGenreId() {
        final var expectedName = "Drama";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualOutput = useCase.execute(aCommand);

        final var actualEntity
                = genreRepository.findById(GenreID.from(actualOutput.id()).getValue()).get();

        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualEntity.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualEntity.getCategoryIDs())
        );
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNotNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenInvalidEmptyName_whenCallsCreateGenre_thenReturnDomainException() {
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, times(0)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenInvalidNullName_whenCallsCreateGenre_thenReturnDomainException() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, times(0)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenValidCommand_whenCallsCreateGenreAndCategoriesDoNotExist_thenReturnDomainException() {
        final var aCategory =
                categoryGateway.create(Category.newCategory("Movies", null, true));
        final String expectedName = "Drama";
        final var expectedIsActive = true;
        final var cat2 = CategoryID.from("456");
        final var cat3 = CategoryID.from("789");
        final var expectedCategories = List.<CategoryID>of(aCategory.getId(), cat2, cat3);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Some categories could not be found: 456, 789";

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, times(1)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenInvalidName_whenCallsCreateGenreAndCategoriesDoNotExist_thenReturnDomainException() {
        final var aCategory =
                categoryGateway.create(Category.newCategory("Movies", null, true));
        final String expectedName = " ";
        final var expectedIsActive = true;
        final var cat2 = CategoryID.from("456");
        final var cat3 = CategoryID.from("789");
        final var expectedCategories = List.<CategoryID>of(aCategory.getId(), cat2, cat3);

        final var expectedErrorCount = 2;
        final var expectedErrorMessage1 = "Some categories could not be found: 456, 789";
        final var expectedErrorMessage2 = "'name' should not be empty";

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage1, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).message());

        Mockito.verify(categoryGateway, times(1)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
