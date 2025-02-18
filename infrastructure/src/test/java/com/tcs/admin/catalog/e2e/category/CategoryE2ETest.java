package com.tcs.admin.catalog.e2e.category;

import com.tcs.admin.catalog.E2ETest;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.tcs.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.tcs.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.tcs.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.tcs.admin.catalog.infrastructure.configuration.json.Json;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

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
        this.categoryRepository.deleteAll();
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToCreateNewCategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.categoryRepository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = retrieveACategory(actualId.getValue());

        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertNotNull(actualCategory.createdAt());
        Assertions.assertNotNull(actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToNavigateAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.categoryRepository.count());

        final var actualId1 = givenACategory("Movies", null, true);
        final var actualId2 = givenACategory("Documentaries", null, true);
        final var actualId3 = givenACategory("Series", null, true);

        listCategories(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Documentaries")));

        listCategories(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Movies")));

        listCategories(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Series")));

        listCategories(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(0)));
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToSearchBetweenAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.categoryRepository.count());

        final var actualId1 = givenACategory("Movies", null, true);
        final var actualId2 = givenACategory("Documentaries", null, true);
        final var actualId3 = givenACategory("Series", null, true);

        listCategories(0, 1, "mov")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Movies")));
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.categoryRepository.count());

        givenACategory("Movies", "Movies", true);
        givenACategory("Documentaries", "Docs", true);
        givenACategory("Series", "Series", true);

        listCategories(0, 3, "", "description", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Series")))
                .andExpect(jsonPath("$.items[1].name", Matchers.equalTo("Movies")))
                .andExpect(jsonPath("$.items[2].name", Matchers.equalTo("Documentaries")));
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToGetCategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.categoryRepository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToSeeTreatedErrorByGettingNotFoundCategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.categoryRepository.count());

        final var aRequest = MockMvcRequestBuilders.get("/categories/123")
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.equalTo("Category with ID 123 was not found")));
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToUpdateCategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.categoryRepository.count());

        final var actualId = givenACategory("Movi", null, true);

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var updateCategoryRequest = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var aRequest = MockMvcRequestBuilders.put("/categories/{id}", actualId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(updateCategoryRequest));

        this.mvc.perform(aRequest)
                .andExpect(status().isOk());

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToDeactivateCategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.categoryRepository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = false;

        final var actualId = givenACategory(expectedName, expectedDescription, true);

        final var updateCategoryRequest = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var aRequest = MockMvcRequestBuilders.put("/categories/{id}", actualId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(updateCategoryRequest));

        this.mvc.perform(aRequest)
                .andExpect(status().isOk());

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToActivateCategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.categoryRepository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, false);

        final var updateCategoryRequest = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var aRequest = MockMvcRequestBuilders.put("/categories/{id}", actualId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(updateCategoryRequest));

        this.mvc.perform(aRequest)
                .andExpect(status().isOk());

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToDeleteCategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.categoryRepository.count());

        final var actualId = givenACategory("Movies", null, true);

        final var aRequest = MockMvcRequestBuilders.delete("/categories/{id}", actualId.getValue());

        this.mvc.perform(aRequest)
                .andExpect(status().isNoContent());

        Assertions.assertFalse(categoryRepository.existsById(actualId.getValue()));
    }

    private ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    private ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return listCategories(page, perPage, search, "", "");
    }

    private ResultActions listCategories(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get("/categories")
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc.perform(aRequest);
    }

    private CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var createCategoryRequest = new CreateCategoryRequest(aName, aDescription, isActive);

        final var aRequest = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(createCategoryRequest));

        final var actualId = this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/categories/", "");

        return CategoryID.from(actualId);
    }

    private CategoryResponse retrieveACategory(final String anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get("/categories/{id}", anId)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, CategoryResponse.class);
    }
}
