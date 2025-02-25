package com.tcs.admin.catalog.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.admin.catalog.ControllerTest;
import com.tcs.admin.catalog.application.castmember.create.CreateCastMemberOutput;
import com.tcs.admin.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.tcs.admin.catalog.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.tcs.admin.catalog.application.castmember.retrieve.get.CastMemberOutput;
import com.tcs.admin.catalog.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import com.tcs.admin.catalog.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import com.tcs.admin.catalog.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.domain.validation.Error;
import com.tcs.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        verify(getCastMemberByIdUseCase, times(1)).execute(expectedId.getValue());
    }
}
