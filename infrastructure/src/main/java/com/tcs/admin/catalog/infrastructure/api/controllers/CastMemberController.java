package com.tcs.admin.catalog.infrastructure.api.controllers;

import com.tcs.admin.catalog.application.castmember.create.CreateCastMemberCommand;
import com.tcs.admin.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.infrastructure.api.CastMemberAPI;
import com.tcs.admin.catalog.infrastructure.castmember.models.CastMemberListResponse;
import com.tcs.admin.catalog.infrastructure.castmember.models.CastMemberResponse;
import com.tcs.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.tcs.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;

    public CastMemberController(final CreateCastMemberUseCase createCastMemberUseCase) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
    }

    @Override
    public ResponseEntity<?> create(CreateCastMemberRequest input) {
        final var aCommand =
                CreateCastMemberCommand.with(input.name(), input.type());
        final var output = createCastMemberUseCase.execute(aCommand);
        return ResponseEntity.created(URI.create("/cast_members/" + output.id()))
                .body(output);
    }

    @Override
    public Pagination<CastMemberListResponse> list(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public CastMemberResponse getById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> update(String id, UpdateCastMemberRequest input) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
