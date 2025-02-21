package com.tcs.admin.catalog.domain.castmember;

import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CastMemberTest {

    @Test
    public void givenValidParams_whenCallsNewMember_thenInstantiateCastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
        Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallsNewMember_thenReceiveNotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualNotification = Assertions.assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualNotification);
        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithMoreThan255Length_whenCallsNewMember_thenReceiveNotification() {
        final String expectedName = """
                There are many variations of passages of Lorem Ipsum available, but the majority have suffered
                 alteration in some form, by injected humour, or randomised words which don't look even slightly
                 believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything
                 embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to
                 repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a
                 dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate
                 Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition,
                 injected humour, or non-characteristic words etc.
         """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualNotification = Assertions.assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualNotification);
        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLessThan3Length_whenCallsNewMember_thenReceiveNotification() {
        final var expectedName = "Vi";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualNotification = Assertions.assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualNotification);
        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNullType_whenCallsNewMember_thenReceiveNotification() {
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualNotification = Assertions.assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualNotification);
        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.getErrors().get(0).message());
    }

    @Test
    public void givenValidCastMember_whenCallsUpdate_thenReceiveCastMemberUpdated() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember("ViDiesel", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualCreatedAt = actualMember.getCreatedAt();
        final var actualUpdatedAt = actualMember.getUpdatedAt();

        actualMember.update(expectedName, expectedType);

        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(actualCreatedAt, actualMember.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualMember.getUpdatedAt()));
    }

    @Test
    public void givenValidCastMember_whenCallsUpdateWithNullName_thenReceiveNotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualNotification = Assertions.assertThrows(
                NotificationException.class,
                () -> actualMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualNotification);
        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.getErrors().get(0).message());
    }

    @Test
    public void givenValidCastMember_whenCallsUpdateWithEmptyName_thenReceiveNotification() {
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualNotification = Assertions.assertThrows(
                NotificationException.class,
                () -> actualMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualNotification);
        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.getErrors().get(0).message());
    }

    @Test
    public void givenValidCastMember_whenCallsUpdateWithNameWithMoreThan255Length_thenReceiveNotification() {
        final String expectedName = """
                There are many variations of passages of Lorem Ipsum available, but the majority have suffered
                 alteration in some form, by injected humour, or randomised words which don't look even slightly
                 believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything
                 embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to
                 repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a
                 dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate
                 Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition,
                 injected humour, or non-characteristic words etc.
         """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualNotification = Assertions.assertThrows(
                NotificationException.class,
                () -> actualMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualNotification);
        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.getErrors().get(0).message());
    }

    @Test
    public void givenValidCastMember_whenCallsUpdateWithNullType_thenReceiveNotification() {
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualNotification = Assertions.assertThrows(
                NotificationException.class,
                () -> actualMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualNotification);
        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.getErrors().get(0).message());
    }
}
