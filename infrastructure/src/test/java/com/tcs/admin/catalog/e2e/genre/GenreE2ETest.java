package com.tcs.admin.catalog.e2e.genre;

import com.tcs.admin.catalog.E2ETest;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.e2e.MockDsl;
import com.tcs.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GenreRepository genreRepository;

    @Container
    private static final MySQLContainer<?> MYSQL_CONTAINER
            = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("videos_adm");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @BeforeEach
    void cleanUp() {
        this.genreRepository.deleteAll();
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToCreateNewGenre() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.categories().size()
                && expectedCategories.containsAll(mapTo(actualGenre.categories(), CategoryID::from))
        );
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToCreateNewGenreWithCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        final var cat1  = givenACategory("Movies", null, true);
        final var cat2  = givenACategory("Series", null, true);

        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(cat1, cat2);

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.categories().size()
                        && expectedCategories.containsAll(mapTo(actualGenre.categories(), CategoryID::from))
        );
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToNavigateAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        givenAGenre("Drama", true, List.of());
        givenAGenre("Action", true, List.of());
        givenAGenre("Scientific Fiction", true, List.of());

        listGenres(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Action")));

        listGenres(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Drama")));

        listGenres(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Scientific Fiction")));

        listGenres(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(0)));
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToSearchBetweenAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        givenAGenre("Drama", true, List.of());
        givenAGenre("Action", true, List.of());
        givenAGenre("Scientific Fiction", true, List.of());

        listGenres(0, 1, "fic")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Scientific Fiction")));
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToSortAllGenresByNameDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        givenAGenre("Drama", true, List.of());
        givenAGenre("Action", true, List.of());
        givenAGenre("Scientific Fiction", true, List.of());

        listGenres(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Scientific Fiction")))
                .andExpect(jsonPath("$.items[1].name", Matchers.equalTo("Drama")))
                .andExpect(jsonPath("$.items[2].name", Matchers.equalTo("Action")));
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToGetGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(
                givenACategory("Movies", null, true)
        );

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.categories().size()
                        && expectedCategories.containsAll(mapTo(actualGenre.categories(), CategoryID::from))
        );
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToSeeTreatedErrorByGettingNotFoundGenre() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        final var aRequest = MockMvcRequestBuilders.get("/genres/123")
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.equalTo("Genre with ID 123 was not found")));
    }
//
//    @Test
//    public void as_aCatalogAdmin_shouldBeAbleToUpdateCategoryByItsIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, this.categoryRepository.count());
//
//        final var actualId = givenACategory("Movi", null, true);
//
//        final var expectedName = "Movies";
//        final var expectedDescription = "Most watched";
//        final var expectedIsActive = true;
//
//        final var updateCategoryRequest = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);
//
//        final var aRequest = MockMvcRequestBuilders.put("/categories/{id}", actualId.getValue())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Json.writeValueAsString(updateCategoryRequest));
//
//        this.mvc.perform(aRequest)
//                .andExpect(status().isOk());
//
//        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();
//
//        Assertions.assertEquals(expectedName, actualCategory.getName());
//        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
//        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
//        Assertions.assertNotNull(actualCategory.getCreatedAt());
//        Assertions.assertNotNull(actualCategory.getUpdatedAt());
//        Assertions.assertNull(actualCategory.getDeletedAt());
//    }
//
//    @Test
//    public void as_aCatalogAdmin_shouldBeAbleToDeactivateCategoryByItsIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, this.categoryRepository.count());
//
//        final var expectedName = "Movies";
//        final var expectedDescription = "Most watched";
//        final var expectedIsActive = false;
//
//        final var actualId = givenACategory(expectedName, expectedDescription, true);
//
//        final var updateCategoryRequest = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);
//
//        final var aRequest = MockMvcRequestBuilders.put("/categories/{id}", actualId.getValue())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Json.writeValueAsString(updateCategoryRequest));
//
//        this.mvc.perform(aRequest)
//                .andExpect(status().isOk());
//
//        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();
//
//        Assertions.assertEquals(expectedName, actualCategory.getName());
//        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
//        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
//        Assertions.assertNotNull(actualCategory.getCreatedAt());
//        Assertions.assertNotNull(actualCategory.getUpdatedAt());
//        Assertions.assertNotNull(actualCategory.getDeletedAt());
//    }
//
//    @Test
//    public void as_aCatalogAdmin_shouldBeAbleToActivateCategoryByItsIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, this.categoryRepository.count());
//
//        final var expectedName = "Movies";
//        final var expectedDescription = "Most watched";
//        final var expectedIsActive = true;
//
//        final var actualId = givenACategory(expectedName, expectedDescription, false);
//
//        final var updateCategoryRequest = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);
//
//        final var aRequest = MockMvcRequestBuilders.put("/categories/{id}", actualId.getValue())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Json.writeValueAsString(updateCategoryRequest));
//
//        this.mvc.perform(aRequest)
//                .andExpect(status().isOk());
//
//        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();
//
//        Assertions.assertEquals(expectedName, actualCategory.getName());
//        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
//        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
//        Assertions.assertNotNull(actualCategory.getCreatedAt());
//        Assertions.assertNotNull(actualCategory.getUpdatedAt());
//        Assertions.assertNull(actualCategory.getDeletedAt());
//    }
//
//    @Test
//    public void as_aCatalogAdmin_shouldBeAbleToDeleteCategoryByItsIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, this.categoryRepository.count());
//
//        final var actualId = givenACategory("Movies", null, true);
//
//        final var aRequest = MockMvcRequestBuilders.delete("/categories/{id}", actualId.getValue());
//
//        this.mvc.perform(aRequest)
//                .andExpect(status().isNoContent());
//
//        Assertions.assertFalse(categoryRepository.existsById(actualId.getValue()));
//    }


}
