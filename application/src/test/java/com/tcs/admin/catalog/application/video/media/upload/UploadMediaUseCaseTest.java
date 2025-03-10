package com.tcs.admin.catalog.application.video.media.upload;

import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.video.MediaResourceGateway;
import com.tcs.admin.catalog.domain.video.MediaType;
import com.tcs.admin.catalog.domain.video.VideoGateway;
import com.tcs.admin.catalog.domain.video.VideoResource;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UploadMediaUseCaseTest {

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Mock
    private VideoGateway videoGateway;

    @InjectMocks
    private DefaultUploadMediaUseCase useCase;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(mediaResourceGateway, videoGateway);
    }

    @Test
    public void givenCmdToUpload_whenIsValid_thenUploadVideoMediaAndPersistIt() {
        final var aVideo = Fixture.Videos.fastAndFurious();
        final var expectedId = aVideo.getId();
        final var expectedType = MediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeAudioVideo(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = this.useCase.execute(aCommand);

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));
        verify(mediaResourceGateway, times(1)).storeAudioVideo(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo ->
                Objects.equals(expectedMedia, actualVideo.getVideo().get())
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_thenUploadTrailerMediaAndPersistIt() {
        final var aVideo = Fixture.Videos.fastAndFurious();
        final var expectedId = aVideo.getId();
        final var expectedType = MediaType.TRAILER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeAudioVideo(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = this.useCase.execute(aCommand);

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));
        verify(mediaResourceGateway, times(1)).storeAudioVideo(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo ->
                Objects.equals(expectedMedia, actualVideo.getTrailer().get())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_thenUploadBannerMediaAndPersistIt() {
        final var aVideo = Fixture.Videos.fastAndFurious();
        final var expectedId = aVideo.getId();
        final var expectedType = MediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeImage(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = this.useCase.execute(aCommand);

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));
        verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo ->
                Objects.equals(expectedMedia, actualVideo.getBanner().get())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_thenUploadThumbnailMediaAndPersistIt() {
        final var aVideo = Fixture.Videos.fastAndFurious();
        final var expectedId = aVideo.getId();
        final var expectedType = MediaType.THUMBNAIL;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeImage(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = this.useCase.execute(aCommand);

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));
        verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo ->
                Objects.equals(expectedMedia, actualVideo.getThumbnail().get())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_thenUploadThumbnailHalfMediaAndPersistIt() {
        final var aVideo = Fixture.Videos.fastAndFurious();
        final var expectedId = aVideo.getId();
        final var expectedType = MediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeImage(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = this.useCase.execute(aCommand);

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));
        verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo ->
                Objects.equals(expectedMedia, actualVideo.getThumbnailHalf().get())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
        ));
    }

    @Test
    public void givenCmdToUpload_whenVideoIsValid_thenReturnNotFound() {
        final var aVideo = Fixture.Videos.fastAndFurious();
        final var expectedId = aVideo.getId();
        final var expectedType = MediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(videoGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> this.useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
