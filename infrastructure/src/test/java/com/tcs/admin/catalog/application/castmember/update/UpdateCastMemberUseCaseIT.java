package com.tcs.admin.catalog.application.castmember.update;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
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
public class UpdateCastMemberUseCaseIT {

    @Autowired
    private UpdateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @BeforeEach
    void cleanUp(){
        this.castMemberRepository.deleteAll();
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCastMember_thenReturnCastMemberId() {
        final var aCastMember = CastMember.newMember("VinDiesel", CastMemberType.DIRECTOR);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember));

        final var expectedId = aCastMember.getId();
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        final var actualOutput = useCase.execute(aCommand);

        final var actualEntity
                = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedType, actualEntity.getType());
        Assertions.assertEquals(aCastMember.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(aCastMember.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));

        Mockito.verify(castMemberGateway, times(1)).findById(any());
        Mockito.verify(castMemberGateway, times(1)).update(any());
    }

    @Test
    public void givenInvalidName_whenCallsUpdateCastMember_thenReturnNotificationException() {
        final var aCastMember = CastMember.newMember("Vin Diesel", CastMemberType.ACTOR);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember));

        final var expectedId = aCastMember.getId();
        final var expectedName = " ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                CastMemberType.ACTOR
        );

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, times(1)).findById(expectedId);
        Mockito.verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenInvalidNameAndType_whenCallsUpdateCastMember_thenReturnNotificationException() {
        final String expectedName = null;
        final CastMemberType expectedType = null;

        final var aCastMember = CastMember.newMember("Vin Diesel", CastMemberType.ACTOR);
        final var expectedId = aCastMember.getId();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember));

        final var expectedErrorCount = 2;
        final var expectedErrorMessage1 = "'name' should not be null";
        final var expectedErrorMessage2 = "'type' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage1, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).message());

        Mockito.verify(castMemberGateway, times(1)).findById(expectedId);
        Mockito.verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenInvalidId_whenCallsUpdateCastMember_thenReturnNotFound() {
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID %s was not found".formatted(expectedId.getValue());

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                "Vin Diesel",
                CastMemberType.ACTOR
        );

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGateway, times(1)).findById(expectedId);
        Mockito.verify(castMemberGateway, times(0)).update(any());
    }
}
