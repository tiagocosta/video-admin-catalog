package com.tcs.admin.catalog.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.admin.catalog.ControllerTest;
import com.tcs.admin.catalog.application.video.create.CreateVideoCommand;
import com.tcs.admin.catalog.application.video.create.CreateVideoOutput;
import com.tcs.admin.catalog.application.video.create.CreateVideoUseCase;
import com.tcs.admin.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.tcs.admin.catalog.application.video.retrieve.get.VideoOutput;
import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.genre.GenreID;
import com.tcs.admin.catalog.domain.video.Video;
import com.tcs.admin.catalog.domain.video.VideoID;
import com.tcs.admin.catalog.infrastructure.video.models.CreateVideoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Set;

import static com.tcs.admin.catalog.domain.utils.CollectionUtils.mapTo;
import static com.tcs.admin.catalog.domain.video.MediaType.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoAPI.class)
class VideoAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CreateVideoUseCase createVideoUseCase;

    @MockitoBean
    private GetVideoByIdUseCase getVideoByIdUseCase;

    @Test
    public void givenValidCommand_whenCallsCreateFull_thenReturnIt() throws Exception {
        final var mateus = Fixture.CastMembers.mateus();
        final var drama = Fixture.Genres.drama();
        final var prime = Fixture.Categories.prime();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(prime.getId().getValue());
        final var expectedGenres = Set.of(drama.getId().getValue());
        final var expectedCastMembers = Set.of(mateus.getId().getValue());

        final var expectedVideo =
                new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());
        final var expectedTrailer =
                new MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER".getBytes());
        final var expectedBanner =
                new MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".getBytes());
        final var expectedThumb =
                new MockMultipartFile("thumb_file", "thumbnail.jpg", "image/jpg", "THUMB".getBytes());
        final var expectedThumbHalf =
                new MockMultipartFile("thumb_half_file", "thumb_half.mp4", "image/jpg", "THUMB_HALF".getBytes());

        when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        final var aRequest = multipart("/videos")
                .file(expectedVideo)
                .file(expectedTrailer)
                .file(expectedBanner)
                .file(expectedThumb)
                .file(expectedThumbHalf)
                .param("title", expectedTitle)
                .param("description", expectedDescription)
                .param("year_launched", String.valueOf(expectedLaunchYear.getValue()))
                .param("duration", expectedDuration.toString())
                .param("opened", String.valueOf(expectedOpened))
                .param("published", String.valueOf(expectedPublished))
                .param("rating", expectedRating.getName())
                .param("cast_members_id", mateus.getId().getValue())
                .param("categories_id", prime.getId().getValue())
                .param("genres_id", drama.getId().getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(expectedTitle, actualCmd.title());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCmd.duration());
        Assertions.assertEquals(expectedOpened, actualCmd.opened());
        Assertions.assertEquals(expectedPublished, actualCmd.published());
        Assertions.assertEquals(expectedRating.getName(), actualCmd.rating());
        Assertions.assertEquals(expectedCategories, actualCmd.categories());
        Assertions.assertEquals(expectedGenres, actualCmd.genres());
        Assertions.assertEquals(expectedCastMembers, actualCmd.castMembers());
        Assertions.assertEquals(expectedVideo.getOriginalFilename(), actualCmd.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.getOriginalFilename(), actualCmd.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.getOriginalFilename(), actualCmd.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.getOriginalFilename(), actualCmd.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.getOriginalFilename(), actualCmd.getThumbnailHalf().get().name());
    }

    @Test
    public void givenValidCommand_whenCallsCreateDraft_thenReturnIt() throws Exception {
        final var mateus = Fixture.CastMembers.mateus();
        final var drama = Fixture.Genres.drama();
        final var prime = Fixture.Categories.prime();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(prime.getId().getValue());
        final var expectedGenres = Set.of(drama.getId().getValue());
        final var expectedCastMembers = Set.of(mateus.getId().getValue());

        final var aCommand = new CreateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        final var aRequest = post("/videos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(expectedTitle, actualCmd.title());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCmd.duration());
        Assertions.assertEquals(expectedOpened, actualCmd.opened());
        Assertions.assertEquals(expectedPublished, actualCmd.published());
        Assertions.assertEquals(expectedRating.getName(), actualCmd.rating());
        Assertions.assertEquals(expectedCategories, actualCmd.categories());
        Assertions.assertEquals(expectedGenres, actualCmd.genres());
        Assertions.assertEquals(expectedCastMembers, actualCmd.castMembers());
        Assertions.assertTrue(actualCmd.getVideo().isEmpty());
        Assertions.assertTrue(actualCmd.getTrailer().isEmpty());
        Assertions.assertTrue(actualCmd.getBanner().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnail().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnailHalf().isEmpty());
    }

    @Test
    public void givenValidId_whenCallsGetVideoById_thenReturnVideo() throws Exception {
        final var mateus = Fixture.CastMembers.mateus();
        final var drama = Fixture.Genres.drama();
        final var prime = Fixture.Categories.prime();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(prime.getId().getValue());
        final var expectedGenres = Set.of(drama.getId().getValue());
        final var expectedCastMembers = Set.of(mateus.getId().getValue());

        final var expectedVideo = Fixture.Videos.audioVideo(VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideo(TRAILER);
        final var expectedBanner = Fixture.Videos.image(BANNER);
        final var expectedThumb = Fixture.Videos.image(THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.image(THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedRating,
                        expectedOpened,
                        expectedPublished,
                        mapTo(expectedCategories, CategoryID::from),
                        mapTo(expectedGenres, GenreID::from),
                        mapTo(expectedCastMembers, CastMemberID::from)
                )
                .updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumb)
                .updateThumbnailHalfMedia(expectedThumbHalf);

        final var expectedId = aVideo.getId().getValue();

        when(getVideoByIdUseCase.execute(any()))
                .thenReturn(VideoOutput.from(aVideo));

        final var aRequest = get("/videos/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var aResponse = this.mvc.perform(aRequest);

        aResponse.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.year_launched", equalTo(expectedLaunchYear.getValue())))
                .andExpect(jsonPath("$.duration", equalTo(expectedDuration)))
                .andExpect(jsonPath("$.opened", equalTo(expectedOpened)))
                .andExpect(jsonPath("$.published", equalTo(expectedPublished)))
                .andExpect(jsonPath("$.rating", equalTo(expectedRating.getName())))
                .andExpect(jsonPath("$.created_at", equalTo(aVideo.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aVideo.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.banner.id", equalTo(expectedBanner.id())))
                .andExpect(jsonPath("$.banner.name", equalTo(expectedBanner.name())))
                .andExpect(jsonPath("$.banner.location", equalTo(expectedBanner.location())))
                .andExpect(jsonPath("$.banner.checksum", equalTo(expectedBanner.checksum())))
                .andExpect(jsonPath("$.thumbnail.id", equalTo(expectedThumb.id())))
                .andExpect(jsonPath("$.thumbnail.name", equalTo(expectedThumb.name())))
                .andExpect(jsonPath("$.thumbnail.location", equalTo(expectedThumb.location())))
                .andExpect(jsonPath("$.thumbnail.checksum", equalTo(expectedThumb.checksum())))
                .andExpect(jsonPath("$.thumbnail_half.id", equalTo(expectedThumbHalf.id())))
                .andExpect(jsonPath("$.thumbnail_half.name", equalTo(expectedThumbHalf.name())))
                .andExpect(jsonPath("$.thumbnail_half.location", equalTo(expectedThumbHalf.location())))
                .andExpect(jsonPath("$.thumbnail_half.checksum", equalTo(expectedThumbHalf.checksum())))
                .andExpect(jsonPath("$.video.id", equalTo(expectedVideo.id())))
                .andExpect(jsonPath("$.video.name", equalTo(expectedVideo.name())))
                .andExpect(jsonPath("$.video.checksum", equalTo(expectedVideo.checksum())))
                .andExpect(jsonPath("$.video.location", equalTo(expectedVideo.rawLocation())))
                .andExpect(jsonPath("$.video.encoded_location", equalTo(expectedVideo.encodedLocation())))
                .andExpect(jsonPath("$.video.status", equalTo(expectedVideo.status().name())))
                .andExpect(jsonPath("$.trailer.id", equalTo(expectedTrailer.id())))
                .andExpect(jsonPath("$.trailer.name", equalTo(expectedTrailer.name())))
                .andExpect(jsonPath("$.trailer.checksum", equalTo(expectedTrailer.checksum())))
                .andExpect(jsonPath("$.trailer.location", equalTo(expectedTrailer.rawLocation())))
                .andExpect(jsonPath("$.trailer.encoded_location", equalTo(expectedTrailer.encodedLocation())))
                .andExpect(jsonPath("$.trailer.status", equalTo(expectedTrailer.status().name())))
                .andExpect(jsonPath("$.categories_id", equalTo(new ArrayList(expectedCategories))))
                .andExpect(jsonPath("$.genres_id", equalTo(new ArrayList(expectedGenres))))
                .andExpect(jsonPath("$.cast_members_id", equalTo(new ArrayList(expectedCastMembers))));
    }
}