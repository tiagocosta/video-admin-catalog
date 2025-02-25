package com.tcs.admin.catalog.domain.video;

import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.genre.GenreID;
import com.tcs.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

public class VideoTest {

    @Test
    public void givenValidParams_whenCallsNewVideo_thenInstantiateIt() {
        final var expectedTitle = "Video Title";
        final var expectedDescription = "Video description";
        final var expectedLaunchedAt = Year.of(2025);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.F;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }
}
