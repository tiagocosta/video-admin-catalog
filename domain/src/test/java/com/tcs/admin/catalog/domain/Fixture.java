package com.tcs.admin.catalog.domain;

import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.resource.Resource;
import com.tcs.admin.catalog.domain.utils.IdUtils;
import com.tcs.admin.catalog.domain.video.*;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.Year;
import java.util.Set;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Instant instant() {
        return FAKER.timeAndDate().between(
                Instant.ofEpochSecond(Instant.MIN.getEpochSecond()),
                Instant.ofEpochSecond(Instant.MIN.getEpochSecond())
        );
    }

    public static Year year() {
        return Year.of(FAKER.random().nextInt(2020, 2030));
    }

    public static Double duration() {
        return BigDecimal.valueOf(FAKER.random().nextDouble() * 300)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static String title() {
        return FAKER.options().option(
                "Video Title 1",
                "Video Title 2",
                "Video Title 3"
        );
    }

    public static String location() {
        return FAKER.options().option(
                "/media"
        );
    }

    public static String checksum() {
        return FAKER.random().hex();
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static MediaStatus mediaStatus() {
        return FAKER.options().option(MediaStatus.values());
    }

    public static MediaType mediaType() {
        return FAKER.options().option(MediaType.values());
    }

    public static Video video() {
        return Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Fixture.year(),
                Fixture.duration(),
                Fixture.Videos.rating(),
                Fixture.bool(),
                Fixture.bool(),
                Set.of(Fixture.Categories.prime().getId()),
                Set.of(Fixture.Genres.drama().getId()),
                Set.of(Fixture.CastMembers.tiago().getId())
        );
    }

    public static VideoPreview videoPreview() {
        return new VideoPreview(
                IdUtils.uuid(),
                Fixture.title(),
                Fixture.Videos.description(),
                Fixture.instant(),
                Fixture.instant()
        );
    }

    public static AudioVideoMedia videoMedia() {
        return AudioVideoMedia.with(
                Fixture.checksum(),
                Fixture.name(),
                Fixture.location()
        );
    }

    public static ImageMedia imageMedia() {
        return ImageMedia.with(
                Fixture.checksum(),
                Fixture.name(),
                Fixture.location()
        );
    }

    public static final class Categories {

        private static final Category PRIME =
                Category.newCategory("Prime", "Amazon Prime", true);

        private static final Category NETFLIX =
                Category.newCategory("Netflix", "Netflix", true);

        public static Category prime() {
            return Category.with(PRIME);
        }

        public static Category netflix() {
            return Category.with(NETFLIX);
        }
    }

    public static final class Genres {

        private static final Genre DRAMA =
                Genre.newGenre("Drama", true);

        private static final Genre ROMANCE =
                Genre.newGenre("Romance", true);

        public static Genre drama() {
            return Genre.with(DRAMA);
        }

        public static Genre romance() {
            return Genre.with(ROMANCE);
        }
    }

    public static final class CastMembers {

        private static final CastMember TIAGO =
                CastMember.newMember("Tiago", CastMemberType.DIRECTOR);

        private static final CastMember MATEUS =
                CastMember.newMember("Mateus", CastMemberType.ACTOR);

        private static final CastMember LUCAS =
                CastMember.newMember("Lucas", CastMemberType.ACTOR);

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }

        public static CastMember tiago() {
            return CastMember.with(TIAGO);
        }

        public static CastMember mateus() {
            return CastMember.with(MATEUS);
        }

        public static CastMember lucas() {
            return CastMember.with(LUCAS);
        }
    }

    public static final class Videos {

        public static String description() {
            return FAKER.options().option(
                    "Video Description 1",
                    "Video Description 2",
                    "Video Description 3"
            );
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static Resource resource(final MediaType type) {
            final String contentType = switch (type) {
                case VIDEO, TRAILER -> "vide/mp4";
                default -> "image/jpg";
            };

            final var checksum = IdUtils.uuid();
            final var content = "Content".getBytes();

            return Resource.with(checksum, content, contentType, type.name().toLowerCase());
        }
    }
}
