package com.tcs.admin.catalog.infrastructure.genre;

import com.tcs.admin.catalog.MySQLGatewayTest;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.infrastructure.category.CategoryMySQLGateway;
import com.tcs.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void cleanUp() {
        this.genreRepository.deleteAll();
    }

    @Test
    public void givenValidGenre_whenCallCreate_thenReturnNewGenre() {
        final var aCategory = categoryGateway.create(
          Category.newCategory("Movies", null, true)
        );

        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(aCategory.getId());

        final var aGenre
                = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var actualGenre = genreGateway.create(aGenre);

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(aGenre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualEntity
                = genreRepository.findById(aGenre.getId().getValue()).get();

        Assertions.assertEquals(aGenre.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(expectedCategories, actualEntity.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenValidGenre_whenCallCreateWithNoCategories_thenReturnNewGenre() {
        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre
                = Genre.newGenre(expectedName, expectedIsActive);

        final var actualGenre = genreGateway.create(aGenre);

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(aGenre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualEntity
                = genreRepository.findById(aGenre.getId().getValue()).get();

        Assertions.assertEquals(aGenre.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(expectedCategories, actualEntity.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }
}
