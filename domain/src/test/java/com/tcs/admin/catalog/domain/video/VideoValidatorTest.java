package com.tcs.admin.catalog.domain.video;

import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.DomainException;
import com.tcs.admin.catalog.domain.genre.GenreID;
import com.tcs.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

public class VideoValidatorTest {

    @Test
    public void givenNullTitle_whenCallsValidate_thenReceiveError() {
        final String expectedTitle = null;
        final var expectedDescription = "Video description";
        final var expectedLaunchedAt = Year.of(2025);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.F;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be null";

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

        final var aValidator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> aValidator.validate()
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyTitle_whenCallsValidate_thenReceiveError() {
        final var expectedTitle = " ";
        final var expectedDescription = "Video description";
        final var expectedLaunchedAt = Year.of(2025);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.F;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

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

        final var aValidator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> aValidator.validate()
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenTitleWithLengthGreaterThan255_whenCallsValidate_thenReceiveError() {
        final var expectedTitle = """
                Lorem ipsum dolor sit amet. Aut placeat harum sit aliquid quae qui alias perspiciatis At molestiae dolorem.
                Et sapiente temporibus a similique ratione non quod asperiores qui amet magni aut consequatur totam aut
                debitis natus At dolor omnis? Ut nisi rerum aut consequatur dolores ab quam magnam qui magnam
                perferendis. Aut nobis laboriosam et incidunt corporis et expedita alias qui quia consequatur. Aut omnis
                beatae id praesentium suscipit et sapiente ipsa? Eum eius omnis est error harum ad vitae quia aut
                consequatur corporis. Ea fugit repudiandae hic illo internos in consequatur cupiditate est cupiditate
                neque in reprehenderit reiciendis.
                """;
        final var expectedDescription = "Video description";
        final var expectedLaunchedAt = Year.of(2025);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.F;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' must be between 1 and 255 characters";

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

        final var aValidator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> aValidator.validate()
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyDescription_whenCallsValidate_thenReceiveError() {
        final var expectedTitle = "Video Title";
        final var expectedDescription = " ";
        final var expectedLaunchedAt = Year.of(2025);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.F;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be empty";

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

        final var aValidator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> aValidator.validate()
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenDescriptionWithLengthGreaterThan4000_whenCallsValidate_thenReceiveError() {
        final var expectedTitle = "Video Title";
        final var expectedDescription = """
                Lorem ipsum dolor sit amet. Aut placeat harum sit aliquid quae qui alias perspiciatis At molestiae dolorem.
                Et sapiente temporibus a similique ratione non quod asperiores qui amet magni aut consequatur totam aut
                debitis natus At dolor omnis? Ut nisi rerum aut consequatur dolores ab quam magnam qui magnam
                perferendis. Aut nobis laboriosam et incidunt corporis et expedita alias qui quia consequatur. Aut omnis
                beatae id praesentium suscipit et sapiente ipsa? Eum eius omnis est error harum ad vitae quia aut
                consequatur corporis. Ea fugit repudiandae hic illo internos in consequatur cupiditate est cupiditate
                neque in reprehenderit reiciendis.
                Lorem ipsum dolor sit amet. Aut placeat harum sit aliquid quae qui alias perspiciatis At molestiae dolorem.
                Et sapiente temporibus a similique ratione non quod asperiores qui amet magni aut consequatur totam aut
                debitis natus At dolor omnis? Ut nisi rerum aut consequatur dolores ab quam magnam qui magnam
                perferendis. Aut nobis laboriosam et incidunt corporis et expedita alias qui quia consequatur. Aut omnis
                beatae id praesentium suscipit et sapiente ipsa? Eum eius omnis est error harum ad vitae quia aut
                consequatur corporis. Ea fugit repudiandae hic illo internos in consequatur cupiditate est cupiditate
                neque in reprehenderit reiciendis.
                Lorem ipsum dolor sit amet. Aut placeat harum sit aliquid quae qui alias perspiciatis At molestiae dolorem.
                Et sapiente temporibus a similique ratione non quod asperiores qui amet magni aut consequatur totam aut
                debitis natus At dolor omnis? Ut nisi rerum aut consequatur dolores ab quam magnam qui magnam
                perferendis. Aut nobis laboriosam et incidunt corporis et expedita alias qui quia consequatur. Aut omnis
                beatae id praesentium suscipit et sapiente ipsa? Eum eius omnis est error harum ad vitae quia aut
                consequatur corporis. Ea fugit repudiandae hic illo internos in consequatur cupiditate est cupiditate
                neque in reprehenderit reiciendis.
                Lorem ipsum dolor sit amet. Aut placeat harum sit aliquid quae qui alias perspiciatis At molestiae dolorem.
                Et sapiente temporibus a similique ratione non quod asperiores qui amet magni aut consequatur totam aut
                debitis natus At dolor omnis? Ut nisi rerum aut consequatur dolores ab quam magnam qui magnam
                perferendis. Aut nobis laboriosam et incidunt corporis et expedita alias qui quia consequatur. Aut omnis
                beatae id praesentium suscipit et sapiente ipsa? Eum eius omnis est error harum ad vitae quia aut
                consequatur corporis. Ea fugit repudiandae hic illo internos in consequatur cupiditate est cupiditate
                neque in reprehenderit reiciendis.
                Lorem ipsum dolor sit amet. Aut placeat harum sit aliquid quae qui alias perspiciatis At molestiae dolorem.
                Et sapiente temporibus a similique ratione non quod asperiores qui amet magni aut consequatur totam aut
                debitis natus At dolor omnis? Ut nisi rerum aut consequatur dolores ab quam magnam qui magnam
                perferendis. Aut nobis laboriosam et incidunt corporis et expedita alias qui quia consequatur. Aut omnis
                beatae id praesentium suscipit et sapiente ipsa? Eum eius omnis est error harum ad vitae quia aut
                consequatur corporis. Ea fugit repudiandae hic illo internos in consequatur cupiditate est cupiditate
                neque in reprehenderit reiciendis.
                Lorem ipsum dolor sit amet. Aut placeat harum sit aliquid quae qui alias perspiciatis At molestiae dolorem.
                Et sapiente temporibus a similique ratione non quod asperiores qui amet magni aut consequatur totam aut
                debitis natus At dolor omnis? Ut nisi rerum aut consequatur dolores ab quam magnam qui magnam
                perferendis. Aut nobis laboriosam et incidunt corporis et expedita alias qui quia consequatur. Aut omnis
                beatae id praesentium suscipit et sapiente ipsa? Eum eius omnis est error harum ad vitae quia aut
                consequatur corporis. Ea fugit repudiandae hic illo internos in consequatur cupiditate est cupiditate
                neque in reprehenderit reiciendis.
                Lorem ipsum dolor sit amet. Aut placeat harum sit aliquid quae qui alias perspiciatis At molestiae dolorem.
                Et sapiente temporibus a similique ratione non quod asperiores qui amet magni aut consequatur totam aut
                debitis natus At dolor omnis? Ut nisi rerum aut consequatur dolores ab quam magnam qui magnam
                perferendis. Aut nobis laboriosam et incidunt corporis et expedita alias qui quia consequatur. Aut omnis
                beatae id praesentium suscipit et sapiente ipsa? Eum eius omnis est error harum ad vitae quia aut
                consequatur corporis. Ea fugit repudiandae hic illo internos in consequatur cupiditate est cupiditate
                neque in reprehenderit reiciendis.
                Lorem ipsum dolor sit amet. Aut placeat harum sit aliquid quae qui alias perspiciatis At molestiae dolorem.
                Et sapiente temporibus a similique ratione non quod asperiores qui amet magni aut consequatur totam aut
                debitis natus At dolor omnis? Ut nisi rerum aut consequatur dolores ab quam magnam qui magnam
                perferendis. Aut nobis laboriosam et incidunt corporis et expedita alias qui quia consequatur. Aut omnis
                beatae id praesentium suscipit et sapiente ipsa? Eum eius omnis est error harum ad vitae quia aut
                consequatur corporis. Ea fugit repudiandae hic illo internos in consequatur cupiditate est cupiditate
                neque in reprehenderit reiciendis.
                """;
        final var expectedLaunchedAt = Year.of(2025);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.F;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' must be between 1 and 4000 characters";

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

        final var aValidator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> aValidator.validate()
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullLaunchedAt_whenCallsValidate_thenReceiveError() {
        final var expectedTitle = "Video Title";
        final var expectedDescription = "Video description";
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.F;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' should not be null";

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

        final var aValidator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> aValidator.validate()
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullRating_whenCallsValidate_thenReceiveError() {
        final var expectedTitle = "Video Title";
        final var expectedDescription = "Video description";
        final var expectedLaunchedAt = Year.of(2025);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be null";

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

        final var aValidator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> aValidator.validate()
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

}
