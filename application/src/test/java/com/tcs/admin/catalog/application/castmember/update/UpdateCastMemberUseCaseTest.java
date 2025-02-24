package com.tcs.admin.catalog.application.castmember.update;

import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCastMemberUseCaseTest {

    @Mock
    private CastMemberGateway castMemberGateway;

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(castMemberGateway);
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCastMember_thenReturnCastMemberId() {
        final var aCastMember = CastMember.newMember("Vin Diesl", CastMemberType.DIRECTOR);
        final var expectedId = aCastMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aCommand =
                UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(CastMember.with(aCastMember)));

        when(castMemberGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        verify(castMemberGateway).findById(expectedId);
        verify(castMemberGateway).update(argThat(aMember ->
            Objects.nonNull(aMember.getId())
            && Objects.equals(expectedName, aMember.getName())
            && Objects.equals(expectedType, aMember.getType())
            && Objects.equals(aCastMember.getCreatedAt(), aMember.getCreatedAt())
            && aCastMember.getUpdatedAt().isBefore(aMember.getUpdatedAt())
        ));
    }

    @Test
    public void givenInvalidNullName_whenCallsUpdateCastMember_thenThrowsNotificationException() {
        final var aCastMember = CastMember.newMember("Vin Diesl", CastMemberType.DIRECTOR);
        final var expectedId = aCastMember.getId();
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand =
                UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(CastMember.with(aCastMember)));

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
    public void givenInvalidNullType_whenCallsUpdateCastMember_thenThrowsNotificationException() {
        final var aCastMember = CastMember.newMember("Vin Diesl", CastMemberType.DIRECTOR);
        final var expectedId = aCastMember.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var aCommand =
                UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(CastMember.with(aCastMember)));

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
    public void givenInvalidCastMemberId_whenCallsUpdateCastMember_thenThrowsNotificationException() {
        final var aCastMember = CastMember.newMember("Vin Diesl", CastMemberType.DIRECTOR);
        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommand =
                UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGateway, times(1)).findById(expectedId);
        Mockito.verify(castMemberGateway, times(0)).update(any());
    }

}
