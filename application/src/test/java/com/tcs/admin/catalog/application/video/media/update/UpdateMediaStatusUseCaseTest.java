package com.tcs.admin.catalog.application.video.media.update;

import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.video.MediaStatus;
import com.tcs.admin.catalog.domain.video.MediaType;
import com.tcs.admin.catalog.domain.video.Video;
import com.tcs.admin.catalog.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateMediaStatusUseCaseTest {

    @Mock
    private VideoGateway videoGateway;

    @InjectMocks
    private DefaultUpdateMediaStatusUseCase useCase;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(videoGateway);
    }

    @Test
    public void givenCmdToUpdateVideo_whenIsValid_thenUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = MediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.fastAndFurious()
                .setVideo(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = UpdateMediaStatusCommand.with(
                expectedId.getValue(),
                expectedMedia.id(),
                expectedStatus,
                expectedFolder,
                expectedFilename
        );

        this.useCase.execute(aCommand);

        verify(videoGateway, times(1)).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();

        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());

        final var actualVideoMedia = actualVideo.getVideo().get();

        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertEquals(
                expectedFolder.concat("/").concat(expectedFilename),
                actualVideoMedia.encodedLocation()
        );
    }

    @Test
    public void givenCmdToUpdateTrailer_whenIsValid_thenUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = MediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.fastAndFurious()
                .setTrailer(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = UpdateMediaStatusCommand.with(
                expectedId.getValue(),
                expectedMedia.id(),
                expectedStatus,
                expectedFolder,
                expectedFilename
        );

        this.useCase.execute(aCommand);

        verify(videoGateway, times(1)).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();

        Assertions.assertTrue(actualVideo.getVideo().isEmpty());

        final var actualVideoMedia = actualVideo.getTrailer().get();

        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertEquals(
                expectedFolder.concat("/").concat(expectedFilename),
                actualVideoMedia.encodedLocation()
        );
    }

    @Test
    public void givenCmdToUpdateTrailer_whenIsValidForProcessing_thenUpdateStatus() {
        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = null;
        final String expectedFilename = null;
        final var expectedType = MediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.fastAndFurious()
                .setTrailer(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = UpdateMediaStatusCommand.with(
                expectedId.getValue(),
                expectedMedia.id(),
                expectedStatus,
                expectedFolder,
                expectedFilename
        );

        this.useCase.execute(aCommand);

        verify(videoGateway, times(1)).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();

        Assertions.assertTrue(actualVideo.getVideo().isEmpty());

        final var actualVideoMedia = actualVideo.getTrailer().get();

        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertTrue(actualVideoMedia.encodedLocation().isBlank());
    }

    @Test
    public void givenCmdToUpdateTrailer_whenIsInvalid_thenNothing() {
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = MediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.fastAndFurious()
                .setTrailer(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        final var aCommand = UpdateMediaStatusCommand.with(
                expectedId.getValue(),
                "invalid_id",
                expectedStatus,
                expectedFolder,
                expectedFilename
        );

        this.useCase.execute(aCommand);

        verify(videoGateway, times(0)).update(any());
    }
}
