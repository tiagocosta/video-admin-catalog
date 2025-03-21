package com.tcs.admin.catalog.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.admin.catalog.ApiTest;
import com.tcs.admin.catalog.ControllerTest;
import com.tcs.admin.catalog.application.category.create.CreateCategoryOutput;
import com.tcs.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.tcs.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.tcs.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.tcs.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.tcs.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.tcs.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.tcs.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.tcs.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.DomainException;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.validation.Error;
import com.tcs.admin.catalog.domain.validation.handler.Notification;
import com.tcs.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.tcs.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockitoBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockitoBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockitoBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockitoBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    public void givenValidCommand_whenCallsCreateCategory_thenReturnCategoryId() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var anInput =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(API.Right(CreateCategoryOutput.from("123")));

        final var request = MockMvcRequestBuilders.post("/categories")
                .with(ApiTest.CATEGORIES_JWT)
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", "/categories/123"),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.id", Matchers.equalTo("123"))
                );

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsCreateCategory_thenReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var anInput =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location", Matchers.nullValue()),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.errors", Matchers.hasSize(1)),
                        jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage))
                );

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenInvalidCommand_whenCallsCreateCategory_thenReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var anInput =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.post("/categories")
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location", Matchers.nullValue()),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.errors", Matchers.hasSize(1)),
                        jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)),
                        jsonPath("$.message", Matchers.equalTo(expectedErrorMessage))
                );

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenValidId_whenCallGetCategory_thenReturnCategory() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(any()))
                .thenReturn(CategoryOutput.from(aCategory));

        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(jsonPath("$.description", Matchers.equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", Matchers.equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", Matchers.equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", Matchers.equalTo(aCategory.getDeletedAt())));
    }

    @Test
    public void givenInvalidId_whenCallGetCategory_thenReturnNotFound() throws Exception {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());

        when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCategory_thenReturnCategoryId() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(API.Right(UpdateCategoryOutput.from(expectedId)));

        final var anInput = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedDescription, cmd.description())
                && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdateCategory_thenReturnNotFoundException() throws Exception {
        final var expectedId = "not-found";
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId);

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var anInput = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdateCategory_thenReturnNotification() throws Exception {
        final var expectedId = "123";
        final String expectedName = null;
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var anInput =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location", Matchers.nullValue()),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.errors", Matchers.hasSize(1)),
                        jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage))
                );

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenValidId_whenCallDeleteCategory_thenReturnNoContent() throws Exception {
        final var expectedId = "123";

        doNothing()
                .when(deleteCategoryUseCase).execute(any());

        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        response.andExpect(status().isNoContent());

        verify(deleteCategoryUseCase, times(1)).execute(expectedId);
    }

    @Test
    public void givenValidParams_whenCallListCategories_thenReturnCategories() throws Exception {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var aCategory = Category.newCategory("Movies", null, true);
        final var expectedItems = List.of(CategoryListOutput.from(aCategory));

        when(listCategoriesUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/categories")
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("direction", expectedDirection)
                .queryParam("search", expectedTerms);

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", Matchers.equalTo(aCategory.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo(aCategory.getName())))
                .andExpect(jsonPath("$.items[0].description", Matchers.equalTo(aCategory.getDescription())))
                .andExpect(jsonPath("$.items[0].is_active", Matchers.equalTo(aCategory.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", Matchers.equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", Matchers.equalTo(aCategory.getDeletedAt())));
    }
}
