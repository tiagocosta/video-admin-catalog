package com.tcs.admin.catalog.infrastructure.category;

import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.category.CategorySearchQuery;
import com.tcs.admin.catalog.infrastructure.MySQLGatewayTest;
import com.tcs.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.tcs.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void cleanUp() {
        this.categoryRepository.deleteAll();
    }

    @Test
    public void givenValidCategory_whenCallCreate_thenReturnNewCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var aCategory
                = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = categoryGateway.create(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity
                = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var aCategory
                = Category.newCategory("Movi", null, true);

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        final var anUpdatedCategory= aCategory.clone()
                .update(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = categoryGateway.update(anUpdatedCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenPrePersistedCategoryAndValidCategoryId_whenCallDelete_thenDeleteCategory() {
        final var aCategory
                = Category.newCategory("Movies", "Most watched", true);

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        categoryGateway.deleteById(aCategory.getId());

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenInvalidCategoryId_whenCallDelete_thenDeleteCategory() {
        Assertions.assertEquals(0, categoryRepository.count());

        categoryGateway.deleteById(CategoryID.from("invalid id"));

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenPrePersistedCategoryAndValidCategoryId_whenCallFindById_thenReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var aCategory
                = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryGateway.findById(aCategory.getId()).get();

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategoryIdNotStored_whenCallFindById_thenReturnEmpty() {
        final var actualCategory
                = categoryGateway.findById(CategoryID.from("123"));

        Assertions.assertTrue(actualCategory.isEmpty());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies1 = Category.newCategory("Movies 1", null, true);
        final var movies2 = Category.newCategory("Movies 2", null, true);
        final var movies3 = Category.newCategory("Movies 3", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies1),
                CategoryJpaEntity.from(movies2),
                CategoryJpaEntity.from(movies3)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var aQuery = new CategorySearchQuery(0,1, "", "name", "asc");
        final var actualResult = categoryGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(movies1.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        final var aQuery = new CategorySearchQuery(0,1, "", "name", "asc");
        final var actualResult = categoryGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies1 = Category.newCategory("Movies 1", null, true);
        final var movies2 = Category.newCategory("Movies 2", null, true);
        final var movies3 = Category.newCategory("Movies 3", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies1),
                CategoryJpaEntity.from(movies2),
                CategoryJpaEntity.from(movies3)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        // Page 0
        var aQuery = new CategorySearchQuery(0,1, "", "name", "asc");
        var actualResult = categoryGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(movies1.getId(), actualResult.items().get(0).getId());

        // Page 1
        expectedPage += 1;
        aQuery = new CategorySearchQuery(1,1, "", "name", "asc");
        actualResult = categoryGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(movies2.getId(), actualResult.items().get(0).getId());

        // Page 2
        expectedPage += 1;
        aQuery = new CategorySearchQuery(2,1, "", "name", "asc");
        actualResult = categoryGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(movies3.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchesCategoryNAme_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies1 = Category.newCategory("Movies 1", "Most watched", true);
        final var movies2 = Category.newCategory("Movies 2", "Less watched", true);
        final var movies3 = Category.newCategory("Documentaries", "Popular", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies1),
                CategoryJpaEntity.from(movies2),
                CategoryJpaEntity.from(movies3)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var aQuery = new CategorySearchQuery(0,1, "doc", "name", "asc");
        final var actualResult = categoryGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(movies3.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndMostWatchedTerms_whenCallsFindAllAndTermsMatchesCategoryNAme_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies1 = Category.newCategory("Movies 1", "Most watched", true);
        final var movies2 = Category.newCategory("Movies 2", "Less watched", true);
        final var movies3 = Category.newCategory("Documentaries", "Popular", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies1),
                CategoryJpaEntity.from(movies2),
                CategoryJpaEntity.from(movies3)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var aQuery = new CategorySearchQuery(0,1, "most watched", "name", "asc");
        final var actualResult = categoryGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(movies1.getId(), actualResult.items().get(0).getId());
    }
}
