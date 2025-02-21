package com.tcs.admin.catalog.e2e;

import com.tcs.admin.catalog.domain.Identifier;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.genre.GenreID;
import com.tcs.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.tcs.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.tcs.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.tcs.admin.catalog.infrastructure.configuration.json.Json;
import com.tcs.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.tcs.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.tcs.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {

    MockMvc mvc();

    default CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var createCategoryRequest = new CreateCategoryRequest(aName, aDescription, isActive);
        final var actualId = given("/categories", createCategoryRequest);
        return CategoryID.from(actualId);
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    default ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return listCategories(page, perPage, search, "", "");
    }

    default ResultActions listCategories(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        return this.list("/categories", page, perPage, search, sort, direction);
    }

    default CategoryResponse retrieveACategory(final Identifier anId) throws Exception {
        return this.retrieve("/categories", anId, CategoryResponse.class);
    }

    default ResultActions deleteACategory(final Identifier anId) throws Exception {
        return this.delete("/categories", anId);
    }

    default ResultActions updateACategory(final Identifier anId, final UpdateCategoryRequest aRequest) throws Exception {
        return this.update("/categories", anId, aRequest);
    }

    default GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var createGenreRequest = new CreateGenreRequest(aName, isActive, mapTo(categories, CategoryID::getValue));
        final var actualId = given("/genres", createGenreRequest);
        return GenreID.from(actualId);
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres(page, perPage, "", "", "");
    }

    default ResultActions listGenres(final int page, final int perPage, final String search) throws Exception {
        return listGenres(page, perPage, search, "", "");
    }

    default ResultActions listGenres(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        return this.list("/genres", page, perPage, search, sort, direction);
    }

    default GenreResponse retrieveAGenre(final Identifier anId) throws Exception {
        return this.retrieve("/genres", anId, GenreResponse.class);
    }

    default ResultActions deleteAGenre(final Identifier anId) throws Exception {
        return this.delete("/genres", anId);
    }

    default ResultActions updateAGenre(final Identifier anId, final UpdateGenreRequest aRequest) throws Exception {
        return this.update("/genres", anId, aRequest);
    }

    default <IN, OUT> List<OUT> mapTo(final List<IN> actual, final Function<IN, OUT> mapper) {
        return actual.stream()
                .map(mapper)
                .toList();
    }

    private String given(final String url, final Object requestBody) throws Exception {
        final var aRequest = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(requestBody));

        return Objects.requireNonNull(this.mvc().perform(aRequest)
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse().getHeader("Location"))
                .replace("%s/".formatted(url), "");
    }

    private ResultActions list(
            final String url,
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        final var aRequest = get(url)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest = get("%s/{id}".formatted(url), anId.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions delete(final String url, final Identifier anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete("%s/{id}".formatted(url), anId.getValue());

        return this.mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier anId, final Object requestBody) throws Exception {
        final var aRequest = MockMvcRequestBuilders.put("%s/{id}".formatted(url), anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(requestBody));

        return this.mvc().perform(aRequest);
    }
}
