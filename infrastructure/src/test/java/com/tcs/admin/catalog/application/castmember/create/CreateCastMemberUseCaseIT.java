package com.tcs.admin.catalog.application.castmember.create;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@IntegrationTest
public class CreateCastMemberUseCaseIT {

    @Autowired
    private CreateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository repository;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @BeforeEach
    void cleanUp(){
        this.repository.deleteAll();
    }

    @Test
    public void givenValidCommand_whenCallsCreateCastMember_thenReturnCastMemberId() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var aCommand =
                CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualOutput = this.useCase.execute(aCommand);

        Assertions.assertEquals(1, this.repository.count());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCastMember
                = this.repository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertNotNull(actualCastMember.getCreatedAt());
        Assertions.assertNotNull(actualCastMember.getUpdatedAt());
    }

    @Test
    public void givenInvalidName_whenCallsCreateCastMember_thenReturnDomainException() {
        final var expectedErrorMessage  = "'name' should not be null";
        final var expectedErrorCount  = 1;

        final var aCommand =
                CreateCastMemberCommand.with(null, CastMemberType.ACTOR);

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, times(0)).create(any());
    }

    @Test
    public void givenInvalidType_whenCallsCreateCastMember_thenReturnDomainException() {
        final var expectedErrorMessage  = "'type' should not be null";
        final var expectedErrorCount  = 1;

        final var aCommand =
                CreateCastMemberCommand.with("Vin Diesel", null);

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, times(0)).create(any());
    }
}
