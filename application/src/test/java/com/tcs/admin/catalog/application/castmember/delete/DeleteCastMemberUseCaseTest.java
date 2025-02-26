package com.tcs.admin.catalog.application.castmember.delete;

import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCastMemberUseCaseTest {

    @Mock
    private CastMemberGateway castMemberGateway;

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(castMemberGateway);
    }

    @Test
    public void givenValidId_whenCallsDeleteCastMember_thenShouldDelete() {
        final var aCastMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        final var expectedId = aCastMember.getId();

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        doNothing()
                .when(castMemberGateway).deleteById(any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(expectedId);
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCastMember_thenShouldBeOk() {
        final var expectedId = CastMemberID.from("123");

        doNothing()
                .when(castMemberGateway).deleteById(any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(expectedId);
    }

    @Test
    public void givenValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_thenShouldReceiveException() {
        final var aCastMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        final var expectedId = aCastMember.getId();

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        doThrow(new IllegalStateException("gateway error"))
                .when(castMemberGateway).deleteById(any());

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(expectedId);
    }

}
