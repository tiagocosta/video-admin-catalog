package com.tcs.admin.catalog.application.video.retrieve.get;

import com.tcs.admin.catalog.application.UseCaseTest;
import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.genre.GenreID;
import com.tcs.admin.catalog.domain.utils.IdUtils;
import com.tcs.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetVideoByIdUseCaseTest extends UseCaseTest {

    @Mock
    private VideoGateway videoGateway;

    @InjectMocks
    private DefaultGetVideoByIdUseCase useCase;

    @Override
    protected void cleanUp() {
        Mockito.reset(videoGateway);
    }

    @Test
    public void givenValidId_whenCallsGetVideo_thenReturnIt() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(Fixture.Categories.prime().getId());
        final var expectedGenres = Set.<GenreID>of(Fixture.Genres.drama().getId());
        final var expectedCastMembers = Set.<CastMemberID>of(
                Fixture.CastMembers.mateus().getId(),
                Fixture.CastMembers.lucas().getId()
        );
        final var expectedVideo = audioVideo(Resource.Type.VIDEO);
        final var expectedTrailer = audioVideo(Resource.Type.TRAILER);
        final var expectedBanner = image(Resource.Type.BANNER);
        final var expectedThumb = image(Resource.Type.THUMBNAIL);
        final var expectedThumbHalf = image(Resource.Type.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedRating,
                        expectedOpened,
                        expectedPublished,
                        expectedCategories,
                        expectedGenres,
                        expectedCastMembers
                )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        final var actualOutput = this.useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedTitle, actualOutput.title());
        Assertions.assertEquals(expectedDescription, actualOutput.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualOutput.launchedAt());
        Assertions.assertEquals(expectedDuration, actualOutput.duration());
        Assertions.assertEquals(expectedOpened, actualOutput.opened());
        Assertions.assertEquals(expectedPublished, actualOutput.published());
        Assertions.assertEquals(expectedRating.getName(), actualOutput.rating());
        Assertions.assertEquals(asString(expectedCategories), actualOutput.categories());
        Assertions.assertEquals(asString(expectedGenres), actualOutput.genres());
        Assertions.assertEquals(asString(expectedCastMembers), actualOutput.castMembers());
        Assertions.assertEquals(expectedVideo, actualOutput.video());
        Assertions.assertEquals(expectedTrailer, actualOutput.trailer());
        Assertions.assertEquals(expectedBanner, actualOutput.banner());
        Assertions.assertEquals(expectedThumb, actualOutput.thumbnail());
        Assertions.assertEquals(expectedThumbHalf, actualOutput.thumbnailHalf());
        Assertions.assertEquals(aVideo.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(aVideo.getUpdatedAt(), actualOutput.updatedAt());
    }

    @Test
    public void givenInvalidId_whenCallsGetVideo_thenReturnNotFound() {
        final var expectedErrorMessage = "Video with ID 123 was not found";
        final var expectedId = VideoID.from("123");

        when(videoGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> this.useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private AudioVideoMedia audioVideo(final Resource.Type type) {
        return AudioVideoMedia.with(
                IdUtils.uuid(),
                "123",
                type.name().toLowerCase(),
                "/media/videos/123",
                "encoded",
                MediaStatus.PENDING
        );
    }

    private ImageMedia image(final Resource.Type type) {
        final var checksum = IdUtils.uuid();
        return ImageMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/media/images/" + checksum
        );
    }
}
