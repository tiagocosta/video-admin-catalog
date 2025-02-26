package com.tcs.admin.catalog.domain;

import com.github.javafaker.Faker;
import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.domain.genre.Genre;
import com.tcs.admin.catalog.domain.video.Rating;
import com.tcs.admin.catalog.domain.video.Resource;
import com.tcs.admin.catalog.domain.video.Video;

import java.time.Year;
import java.util.Set;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Year year() {
        return Year.of(FAKER.random().nextInt(2020, 2030));
    }

    public static Double duration() {
        return FAKER.random().nextDouble() * 300;
    }

    public static String title() {
        return FAKER.options().option(
                "Video Title 1",
                "Video Title 2",
                "Video Title 3"
        );
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static final class Categories {

        private static final Category PRIME =
                Category.newCategory("Prime", "Amazon Prime", true);

        public static Category prime() {
            return Category.with(PRIME);
        }
    }

    public static final class Genres {

        private static final Genre DRAMA =
                Genre.newGenre("Drama", true);

        public static Genre drama() {
            return Genre.with(DRAMA);
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

        private static final Video FATS_AND_FURIOUS =
                Video.newVideo(
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

        public static Video fastAndFurious() {
            return Video.with(FATS_AND_FURIOUS);
        }

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

        public static Resource resource(final Resource.Type type) {
            final String contentType = switch (type) {
                case VIDEO, TRAILER -> "vide/mp4";
                default -> "image/jpg";
            };

            final byte[] content = "Content".getBytes();

            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }
    }
}
