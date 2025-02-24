package com.tcs.admin.catalog.application.castmember.retrieve.get;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.tcs.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@IntegrationTest
public class GetCastMemberByIdUseCaseIT {

    @Autowired
    private GetCastMemberByIdUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @BeforeEach
    void cleanUp() {
        this.castMemberRepository.deleteAll();
    }

    @Test
    public void givenValidId_whenCallGetCastMember_thenReturnCastMember() {
        final var expectedName = "Vin diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var aCastMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aCastMember.getId();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember));

        final var actualOutput = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedType, actualOutput.type());
        Assertions.assertEquals(aCastMember.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(aCastMember.getUpdatedAt(), actualOutput.updatedAt());

        Mockito.verify(castMemberGateway, times(1)).findById(any());
    }

    @Test
    public void givenInvalidId_whenCallGetCastMember_thenReturnNotFound() {
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID %s was not found".formatted(expectedId.getValue());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenValidId_whenGatewayThrowsException_thenReturnException() {
        final var expectedErrorMessage = "gateway error";
        final var aCastMember =
                CastMember.newMember("Vin Diesel", CastMemberType.ACTOR);

        final var expectedId = aCastMember.getId();

        doThrow(new IllegalStateException("gateway error"))
                .when(castMemberGateway).findById(eq(expectedId));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGateway, times(1)).findById(eq(expectedId));
    }
}
