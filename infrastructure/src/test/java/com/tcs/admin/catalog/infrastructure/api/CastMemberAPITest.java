package com.tcs.admin.catalog.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.admin.catalog.ApiTest;
import com.tcs.admin.catalog.ControllerTest;
import com.tcs.admin.catalog.application.castmember.create.CreateCastMemberOutput;
import com.tcs.admin.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.tcs.admin.catalog.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.tcs.admin.catalog.application.castmember.retrieve.get.CastMemberOutput;
import com.tcs.admin.catalog.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import com.tcs.admin.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import com.tcs.admin.catalog.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import com.tcs.admin.catalog.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.tcs.admin.catalog.application.castmember.update.UpdateCastMemberOutput;
import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.validation.Error;
import com.tcs.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.tcs.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private DefaultCreateCastMemberUseCase createCastMemberUseCase;

    @MockitoBean
    private DefaultDeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockitoBean
    private DefaultGetCastMemberByIdUseCase getCastMemberByIdUseCase;

    @MockitoBean
    private DefaultListCastMembersUseCase listCastMembersUseCase;

    @MockitoBean
    private DefaultUpdateCastMemberUseCase updateCastMemberUseCase;

    @Test
    public void givenValidCommand_whenCallsCreateCastMember_thenReturnId() throws Exception {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedId = CastMemberID.from("123").getValue();

        final var anInput = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenReturn(CreateCastMemberOutput.from(expectedId));

        final var request = MockMvcRequestBuilders.post("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cast_members/" + expectedId))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId)));

        verify(createCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenInvalidCommand_whenCallsCreateCastMember_thenReturnNotificationException() throws Exception {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be null";

        final var anInput = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.post("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        verify(createCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenValidId_whenCallsGetCastMember_thenReturnIt() throws Exception {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var aCastMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aCastMember.getId().getValue();

        when(getCastMemberByIdUseCase.execute(any()))
                .thenReturn(CastMemberOutput.from(aCastMember));

        final var request = MockMvcRequestBuilders.get("/cast_members/{id}", expectedId)
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(jsonPath("$.type", Matchers.equalTo(expectedType.name())))
                .andExpect(jsonPath("$.created_at", Matchers.equalTo(aCastMember.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", Matchers.equalTo(aCastMember.getUpdatedAt().toString())));

        verify(getCastMemberByIdUseCase, times(1)).execute(expectedId);
    }

    @Test
    public void givenInvalidId_whenCallsGetCastMember_thenReturnNotFound() throws Exception {
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId = CastMemberID.from("123");

        when(getCastMemberByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        verify(getCastMemberByIdUseCase, times(1)).execute(expectedId.getValue());
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCastMember_thenReturnId() throws Exception {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;
        final var aCastMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aCastMember.getId();

        final var anInput = new UpdateCastMemberRequest(expectedName, expectedType);

        when(updateCastMemberUseCase.execute(any()))
                .thenReturn(UpdateCastMemberOutput.from(expectedId.getValue()));

        final var request = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        verify(updateCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId.getValue(), cmd.id())
                        && Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenInvalidCommand_whenCallsUpdateCastMember_thenReturnNotificationException() throws Exception {
        final var aCastMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        final var expectedId = aCastMember.getId();

        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be null";

        final var anInput = new UpdateCastMemberRequest(expectedName, expectedType);

        when(updateCastMemberUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        verify(updateCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId.getValue(), cmd.id())
                        && Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenInvalidId_whenCallsUpdateCastMember_thenReturnNotFound() throws Exception {
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId = CastMemberID.from("123");

        final var anInput = new UpdateCastMemberRequest("Vind", CastMemberType.ACTOR);

        when(updateCastMemberUseCase.execute(any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        final var request = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        verify(updateCastMemberUseCase, times(1)).execute(any());
    }

    @Test
    public void givenValidId_whenCallsDeleteCastMember_thenDeleteIt() throws Exception {
        final var aCastMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        final var expectedId = aCastMember.getId();

        doNothing()
                .when(deleteCastMemberUseCase).execute(any());

        final var request = MockMvcRequestBuilders.delete("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteCastMemberUseCase, times(1)).execute(expectedId.getValue());
    }

    @Test
    public void givenValidParams_whenCallsListCastMember_thenReturnCastMember() throws Exception {
        final var aCastMember = CastMember.newMember("Vin Diesel", CastMemberType.ACTOR);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "sel";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CastMemberListOutput.from(aCastMember));

        when(listCastMembersUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("search", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", Matchers.equalTo(aCastMember.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo(aCastMember.getName())))
                .andExpect(jsonPath("$.items[0].type", Matchers.equalTo(aCastMember.getType().name())))
                .andExpect(jsonPath("$.items[0].created_at", Matchers.equalTo(aCastMember.getCreatedAt().toString())));

        verify(listCastMembersUseCase, times(1)).execute(argThat( query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }

    @Test
    public void givenEmptyParams_whenCallsListCastMember_thenUseDefaultValuesAndReturnCastMembers() throws Exception {
        final var aCastMember1 = CastMember.newMember("Vin Diesel", CastMemberType.ACTOR);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final String expectedTerms = "";
        final String expectedSort = "name";
        final String expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(
                CastMemberListOutput.from(aCastMember1)
        );

        when(listCastMembersUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("search", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", Matchers.equalTo(aCastMember1.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo(aCastMember1.getName())))
                .andExpect(jsonPath("$.items[0].type", Matchers.equalTo(aCastMember1.getType().name())))
                .andExpect(jsonPath("$.items[0].created_at", Matchers.equalTo(aCastMember1.getCreatedAt().toString())));

        verify(listCastMembersUseCase, times(1)).execute(argThat( query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
