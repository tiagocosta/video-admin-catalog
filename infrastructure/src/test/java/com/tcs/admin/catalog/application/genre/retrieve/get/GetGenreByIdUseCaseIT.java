package com.tcs.admin.catalog.application.genre.retrieve.get;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@IntegrationTest
public class GetGenreByIdUseCaseIT {

    @Autowired
    private GetGenreByIdUseCase useCase;

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
    public void givenValidId_whenCallGetGenre_thenReturnGenre() {
        final var cat1 = categoryGateway.create(
                Category.newCategory("Movies", null, true)
        );

        final var cat2 = categoryGateway.create(
                Category.newCategory("Series", null, true)
        );

        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                cat1.getId(),
                cat2.getId()
        );

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        final var actualOutput = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedIsActive, actualOutput.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualOutput.categories().size()
                && asString(expectedCategories).containsAll(actualOutput.categories())
        );
        Assertions.assertEquals(aGenre.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualOutput.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualOutput.deletedAt());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallGetGenre_thenReturnNotFound() {
        final var expectedId = GenreID.from("123");
        final var expectedErrorMessage = "Genre with ID %s was not found".formatted(expectedId.getValue());

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
                Genre.newGenre("Drama", true);

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
