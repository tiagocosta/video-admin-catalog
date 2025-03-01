package com.tcs.admin.catalog.infrastructure.genre;

import com.tcs.admin.catalog.MySQLGatewayTest;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.genre.GenreID;
import com.tcs.admin.catalog.domain.pagination.SearchQuery;
import com.tcs.admin.catalog.infrastructure.category.CategoryMySQLGateway;
import com.tcs.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.tcs.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
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

    @Test
    public void givenValidGenreWithoutCategories_whenCallUpdateGenreWithCategories_thenPersistGenre() {
        final var aCategory1 = categoryGateway.create(
                Category.newCategory("Movies", null, true)
        );

        final var aCategory2 = categoryGateway.create(
                Category.newCategory("Series", null, true)
        );

        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(aCategory1.getId(), aCategory2.getId());

        final var aGenre
                = Genre.newGenre("Dram", expectedIsActive);

        final var updatedAt = aGenre.getUpdatedAt();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        aGenre.update(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = genreGateway.update(Genre.with(aGenre));

        Assertions.assertEquals(aGenre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertIterableEquals(sortCategories(expectedCategories), sortCategories(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(updatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualEntity
                = genreRepository.findById(aGenre.getId().getValue()).get();

        Assertions.assertEquals(aGenre.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(sortCategories(expectedCategories), sortCategories(actualEntity.getCategoryIDs()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertTrue(updatedAt.isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenValidGenreWithCategories_whenCallUpdateGenreCleaningCategories_thenPersistGenre() {
        final var aCategory1 = categoryGateway.create(
                Category.newCategory("Movies", null, true)
        );

        final var aCategory2 = categoryGateway.create(
                Category.newCategory("Series", null, true)
        );

        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre
                = Genre.newGenre("Dram", expectedIsActive);
        aGenre.addCategories(List.of(aCategory1.getId(), aCategory2.getId()));

        final var updatedAt = aGenre.getUpdatedAt();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        aGenre.update(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = genreGateway.update(Genre.with(aGenre));

        Assertions.assertEquals(aGenre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategories(expectedCategories), sortCategories(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(updatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualEntity
                = genreRepository.findById(aGenre.getId().getValue()).get();

        Assertions.assertEquals(aGenre.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(sortCategories(expectedCategories), sortCategories(actualEntity.getCategoryIDs()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertTrue(updatedAt.isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenValidInactiveGenre_whenCallUpdateGenreActivating_thenPersistGenre() {
        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre
                = Genre.newGenre(expectedName, false);

        final var updatedAt = aGenre.getUpdatedAt();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertFalse(aGenre.isActive());
        Assertions.assertNotNull(aGenre.getDeletedAt());

        aGenre.update(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = genreGateway.update(Genre.with(aGenre));

        Assertions.assertEquals(aGenre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(updatedAt.isBefore(actualGenre.getUpdatedAt()));
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
        Assertions.assertTrue(updatedAt.isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenValidActiveGenre_whenCallUpdateGenreDeactivating_thenPersistGenre() {
        final var expectedName = "Drama";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre
                = Genre.newGenre(expectedName, true);

        final var updatedAt = aGenre.getUpdatedAt();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());

        aGenre.update(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = genreGateway.update(Genre.with(aGenre));

        Assertions.assertEquals(aGenre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(updatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var actualEntity
                = genreRepository.findById(aGenre.getId().getValue()).get();

        Assertions.assertEquals(aGenre.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(expectedCategories, actualEntity.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertTrue(updatedAt.isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNotNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenPrePersistedGenre_whenCallsDeleteById_thenDeleteGenre() {
        final var aGenre = Genre.newGenre("Drama", true);

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        genreGateway.deleteById(aGenre.getId());

        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenInvalidId_whenCallsDeleteById_thenReturnOk() {
        Assertions.assertEquals(0, genreRepository.count());

        genreGateway.deleteById(GenreID.from("invalid"));

        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenPrePersistedGenre_whenCallsFindById_thenReturnGenre() {
        final var aCategory1 = categoryGateway.create(
                Category.newCategory("Movies", null, true)
        );

        final var aCategory2 = categoryGateway.create(
                Category.newCategory("Series", null, true)
        );

        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(aCategory1.getId(), aCategory2.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        final var actualGenre = genreGateway.findById(expectedId).get();

        Assertions.assertEquals(aGenre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategories(expectedCategories), sortCategories(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
    }

    @Test
    public void givenInvalidGenreId_whenCallsFindById_thenReturnEmpty() {
        final var actualGenre = genreGateway.findById(GenreID.from("invalid"));

        Assertions.assertTrue(actualGenre.isEmpty());
    }

    @Test
    public void givenEmptyGenres_whenCallsFindAll_thenReturnEmptyList() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "dr,0,10,1,1,Drama",
            "act,0,10,1,1,Action",
            "medy,0,10,1,1,Comedy",
            "fic,0,10,1,1,Scientific Fiction",
            "err,0,10,1,1,Terror",
    })
    public void givenValidTerm_whenCallsFindAll_thenReturnFilteredGenres(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        mockGenres();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Action",
            "name,desc,0,10,5,5,Terror",
            "createdAt,asc,0,10,5,5,Drama",
            "createdAt,desc,0,10,5,5,Terror"
    })
    public void givenValidSortAndDirection_whenCallsFindAll_thenReturnOrderedGenres(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        final var expectedTerms = "";

        mockGenres();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Action;Comedy",
            "1,2,2,5,Drama;Scientific Fiction",
            "2,2,1,5,Terror"
    })
    public void givenValidPaging_whenCallsFindAll_thenReturnPagedGenres(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenres
    ) {
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        mockGenres();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName : expectedGenres.split(";")) {
            Assertions.assertEquals(expectedName, actualPage.items().get(index).getName());
            index += 1;
        }
    }

    private void mockGenres() {
        genreRepository.saveAllAndFlush(List.of(
                GenreJpaEntity.from(Genre.newGenre("Drama", true)),
                GenreJpaEntity.from(Genre.newGenre("Action", true)),
                GenreJpaEntity.from(Genre.newGenre("Comedy", true)),
                GenreJpaEntity.from(Genre.newGenre("Scientific Fiction", true)),
                GenreJpaEntity.from(Genre.newGenre("Terror", true))
        ));
    }

    private List<CategoryID> sortCategories(final List<CategoryID> categories) {
        return categories.stream()
                .sorted(Comparator.comparing(CategoryID::getValue))
                .toList();
    }
}
