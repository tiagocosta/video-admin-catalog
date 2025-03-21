package com.tcs.admin.catalog.application.video.create;

import com.tcs.admin.catalog.application.UseCaseTest;
import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.genre.GenreID;
import com.tcs.admin.catalog.domain.resource.Resource;
import com.tcs.admin.catalog.domain.utils.IdUtils;
import com.tcs.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateVideoUseCaseTest extends UseCaseTest {

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Override
    protected void cleanUp() {
        Mockito.reset(
                videoGateway,
                mediaResourceGateway,
                categoryGateway,
                genreGateway,
                castMemberGateway
        );
    }

    @Test
    public void givenValidCommand_whenCallsCreateVideo_thenReturnVideoId() {
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
        final Resource expectedVideo = Fixture.Videos.resource(MediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(MediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(MediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(MediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(MediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        when(videoGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        mockAudioVideoMedia();
        mockImageMedia();

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.isOpened())
                        && Objects.equals(expectedPublished, actualVideo.isPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedCastMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && Objects.nonNull(actualVideo.getCreatedAt())
                        && Objects.nonNull(actualVideo.getUpdatedAt())
        ));
    }

    @Test
    public void givenValidCommandWithoutCategories_whenCallsCreateVideo_thenReturnVideoId() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of(Fixture.Genres.drama().getId());
        final var expectedCastMembers = Set.<CastMemberID>of(
                Fixture.CastMembers.mateus().getId(),
                Fixture.CastMembers.lucas().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(MediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(MediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(MediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(MediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(MediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        when(videoGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        mockAudioVideoMedia();
        mockImageMedia();

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.isOpened())
                        && Objects.equals(expectedPublished, actualVideo.isPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedCastMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && Objects.nonNull(actualVideo.getCreatedAt())
                        && Objects.nonNull(actualVideo.getUpdatedAt())
        ));
    }

    @Test
    public void givenValidCommandWithoutGenres_whenCallsCreateVideo_thenReturnVideoId() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(Fixture.Categories.prime().getId());
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of(
                Fixture.CastMembers.mateus().getId(),
                Fixture.CastMembers.lucas().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(MediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(MediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(MediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(MediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(MediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        when(videoGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        mockAudioVideoMedia();
        mockImageMedia();

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.isOpened())
                        && Objects.equals(expectedPublished, actualVideo.isPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedCastMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && Objects.nonNull(actualVideo.getCreatedAt())
                        && Objects.nonNull(actualVideo.getUpdatedAt())
        ));
    }

    @Test
    public void givenValidCommandWithoutCastMembers_whenCallsCreateVideo_thenReturnVideoId() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(Fixture.Categories.prime().getId());
        final var expectedGenres = Set.<GenreID>of(Fixture.Genres.drama().getId());
        final var expectedCastMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = Fixture.Videos.resource(MediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(MediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(MediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(MediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(MediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(videoGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        mockAudioVideoMedia();
        mockImageMedia();

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.isOpened())
                        && Objects.equals(expectedPublished, actualVideo.isPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedCastMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && Objects.nonNull(actualVideo.getCreatedAt())
                        && Objects.nonNull(actualVideo.getUpdatedAt())
        ));
    }

    @Test
    public void givenValidCommandWithoutResources_whenCallsCreateVideo_thenReturnVideoId() {
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
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        when(videoGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.isOpened())
                        && Objects.equals(expectedPublished, actualVideo.isPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedCastMembers, actualVideo.getCastMembers())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
                        && Objects.nonNull(actualVideo.getCreatedAt())
                        && Objects.nonNull(actualVideo.getUpdatedAt())
        ));
    }

    @Test
    public void givenNullTitle_whenCallsCreateVideo_thenReturnNotificationException() {
        final String expectedTitle = null;
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be null";

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
    }

    @Test
    public void givenEmptyTitle_whenCallsCreateVideo_thenReturnNotificationException() {
        final var expectedTitle = " ";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
    }

    @Test
    public void givenNullRating_whenCallsCreateVideo_thenReturnNotificationException() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = null;
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be null";

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
    }

    @Test
    public void givenInvalidRating_whenCallsCreateVideo_thenReturnNotificationException() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = "INVALID";
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be null";

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
    }

    @Test
    public void givenNullLaunchYear_whenCallsCreateVideo_thenReturnNotificationException() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final Integer expectedLaunchYear = null;
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' should not be null";

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
    }

    @Test
    public void givenValidCommand_whenCallsCreateVideoAndSomeCategoriesDoNotExist_thenReturnNotificationException() {
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
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage =
                "Some categories could not be found: %s".formatted(Fixture.Categories.prime().getId().getValue());

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(castMemberGateway, times(1)).existsByIds(eq(expectedCastMembers));
    }

    @Test
    public void givenValidCommand_whenCallsCreateVideoAndSomeGenresDoNotExist_thenReturnNotificationException() {
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
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage =
                "Some genres could not be found: %s".formatted(Fixture.Genres.drama().getId().getValue());

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(castMemberGateway, times(1)).existsByIds(eq(expectedCastMembers));
    }

    @Test
    public void givenValidCommand_whenCallsCreateVideoAndSomeCastMembersDoNotExist_thenReturnNotificationException() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(Fixture.Categories.prime().getId());
        final var expectedGenres = Set.<GenreID>of(Fixture.Genres.drama().getId());
        final var expectedCastMembers = Set.<CastMemberID>of(Fixture.CastMembers.lucas().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage =
                "Some cast members could not be found: %s".formatted(Fixture.CastMembers.lucas().getId().getValue());

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(castMemberGateway, times(1)).existsByIds(eq(expectedCastMembers));
    }

    @Test
    public void givenValidCommand_whenCallsCreateVideoThrowsException_thenCallClearResources() {
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
        final Resource expectedVideo = Fixture.Videos.resource(MediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(MediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(MediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(MediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(MediaType.THUMBNAIL_HALF);

        final var expectedErrorMessage = "An error occurred when creating video [videoId:";

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        when(videoGateway.create(any()))
                .thenThrow(new RuntimeException(expectedErrorMessage));

        mockAudioVideoMedia();
        mockImageMedia();

        final var actualException = Assertions.assertThrows(
                RuntimeException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertTrue(actualException.getMessage().startsWith(expectedErrorMessage));

        verify(mediaResourceGateway).clearResources(any());
    }

    private void mockAudioVideoMedia() {
        when(mediaResourceGateway.storeAudioVideo(any(), any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            return AudioVideoMedia.with(
                    IdUtils.uuid(),
                    videoResource.resource().checksum(),
                    videoResource.resource().name(),
                    "/media/img",
                    "/encoded",
                    MediaStatus.PENDING
            );
        });
    }

    private void mockImageMedia() {
        when(mediaResourceGateway.storeImage(any(), any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            return ImageMedia.with(
                    videoResource.resource().checksum(),
                    videoResource.resource().name(),
                    "/media/img"
            );
        });
    }
}
