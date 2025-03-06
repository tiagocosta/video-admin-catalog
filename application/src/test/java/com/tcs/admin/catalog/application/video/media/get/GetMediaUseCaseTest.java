package com.tcs.admin.catalog.application.video.media.get;

import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.video.MediaResourceGateway;
import com.tcs.admin.catalog.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetMediaUseCaseTest {

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @InjectMocks
    private DefaultGetMediaUseCase useCase;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(mediaResourceGateway);
    }

    @Test
    public void givenVideoIdAndType_whenValidCommand_thenReturnResource() {
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.mediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.of(expectedResource));

        final var aCommand = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        final var actualResource = this.useCase.execute(aCommand);

        Assertions.assertEquals(expectedResource.name(), actualResource.name());
        Assertions.assertEquals(expectedResource.content(), actualResource.content());
        Assertions.assertEquals(expectedResource.contentType(), actualResource.contentType());
    }

    @Test
    public void givenVideoIdAndType_whenResourceDoesNotExist_thenReturnNotFoundException() {
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.mediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        final var expectedErrorMessage = "Resource %s not found for video %s".formatted(
                expectedType.name(), expectedId.getValue()
        );

        when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.empty());

        final var aCommand = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> this.useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenVideoIdAndType_whenTypeDoesNotExist_thenReturnNotFoundException() {
        final var expectedErrorMessage = "Media type wrong type does not exist";

        final var aCommand = GetMediaCommand.with("123", "wrong type");

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> this.useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
