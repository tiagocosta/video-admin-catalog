package com.tcs.admin.catalog.application.category.retrieve.list;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.category.CategorySearchQuery;
import com.tcs.admin.catalog.domain.exceptions.DomainException;
import com.tcs.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.tcs.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@IntegrationTest
public class ListCategoriesUseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    void cleanUp(){
        this.categoryRepository.deleteAll();
    }

    @BeforeEach
    void mockUp(){
        cleanUp();
        final var categories = Stream.of(
          Category.newCategory("Movies 1", null, true),
          Category.newCategory("Netflix", "Netflix Productions", true),
          Category.newCategory("Amazon", "Amazon Prime", true),
          Category.newCategory("Documentaries", null, true),
          Category.newCategory("Sports", null, true),
          Category.newCategory("Kids", "For babies and more", true),
          Category.newCategory("Series", null, true)
        )
         .map(CategoryJpaEntity::from)
         .toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    public void givenValidTerm_whenTermDoesNotMatchPrePersisted_thenReturnEmpty() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "abc def";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage,expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "1,0,10,1,1, Movies 1",
            "net,0,10,1,1,Netflix",
            "zon,0,10,1,1,Amazon",
            "KI,0,10,1,1,Kids",
            "babies,0,10,1,1,Kids",
            "prime,0,10,1,1,Amazon",
    })
    public void givenValidTerm_whenTermMatchesPrePersisted_thenReturnFilteredCategories(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon",
            "name,desc,0,10,7,7,Sports",
            "createdAt,asc,0,10,7,7,Movies 1",
            "createdAt,desc,0,10,7,7,Series",
    })
    public void givenValidSortAndDirection_whenCallListCategories_thenReturnFilteredCategories(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedTerms = "";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Amazon;Documentaries",
            "1,2,2,7,Kids;Movies 1",
            "2,2,2,7,Netflix;Series",
            "3,2,1,7,Sports",
    })
    public void givenValidPage_whenCallListCategories_thenReturnPaginatedCategories(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoriesNames
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (final String expectedName : expectedCategoriesNames.split(";")) {
            final var actualName = actualResult.items().get(index).name();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }
}
