package com.tcs.admin.catalog.application.genre.update;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity;
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
public class UpdateGenreUseCaseIT {

    @Autowired
    private UpdateGenreUseCase useCase;

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
    public void givenValidCommand_whenCallsUpdateGenre_thenReturnGenreId() {
        final var aGenre = Genre.newGenre("Dram", true);
        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        final var expectedId = aGenre.getId();
        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        final var actualOutput = useCase.execute(aCommand);

        final var actualEntity
                = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualEntity.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualEntity.getCategoryIDs())
        );
        Assertions.assertEquals(aGenre.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), actualEntity.getDeletedAt());
    }

    @Test
    public void givenValidCommandWithCategories_whenCallsUpdateGenre_thenReturnGenreId() {
        final var aGenre = Genre.newGenre("Dram", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Drama";
        final var expectedIsActive = true;

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        final var cat1 =
                categoryGateway.create(Category.newCategory("Movies", null, true));

        final var cat2 =
                categoryGateway.create(Category.newCategory("Series", null, true));

        final var expectedCategories = List.of(cat1.getId(), cat2.getId());

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        final var actualOutput = useCase.execute(aCommand);

        final var actualEntity
                = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualEntity.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualEntity.getCategoryIDs())
        );
        Assertions.assertEquals(aGenre.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), actualEntity.getDeletedAt());
    }

    @Test
    public void givenValidCommandWithInactiveGenre_whenCallsUpdateGenre_thenReturnGenreId() {
        final var aGenre = Genre.newGenre("Dram", true);
        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        final var expectedId = aGenre.getId();
        final var expectedName = "Drama";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        final var actualOutput = useCase.execute(aCommand);

        final var actualEntity
                = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualEntity.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualEntity.getCategoryIDs())
        );
        Assertions.assertEquals(aGenre.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertNotNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenInvalidName_whenCallsUpdateGenre_thenReturnNotificationException() {
        final var aGenre = Genre.newGenre("Drama", true);
        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
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
        final var aCategory =
                categoryGateway.create(Category.newCategory("Movies", null, true));
        final String expectedName = " ";
        final var expectedIsActive = true;
        final var cat2 = CategoryID.from("456");
        final var cat3 = CategoryID.from("789");

        final var aGenre = Genre.newGenre("Drama", true);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        final var expectedCategories = List.<CategoryID>of(aCategory.getId(), cat2, cat3);
        final var expectedErrorCount = 2;
        final var expectedErrorMessage1 = "Some categories could not be found: 456, 789";
        final var expectedErrorMessage2 = "'name' should not be empty";

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

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
