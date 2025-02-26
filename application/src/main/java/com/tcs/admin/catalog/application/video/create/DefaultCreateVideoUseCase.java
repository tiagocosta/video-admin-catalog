package com.tcs.admin.catalog.application.video.create;

import com.tcs.admin.catalog.domain.Identifier;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.exceptions.DomainException;
import com.tcs.admin.catalog.domain.exceptions.InternalErrorException;
import com.tcs.admin.catalog.domain.exceptions.NotificationException;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.genre.GenreID;
import com.tcs.admin.catalog.domain.validation.Error;
import com.tcs.admin.catalog.domain.validation.ValidationHandler;
import com.tcs.admin.catalog.domain.validation.handler.Notification;
import com.tcs.admin.catalog.domain.video.MediaResourceGateway;
import com.tcs.admin.catalog.domain.video.Rating;
import com.tcs.admin.catalog.domain.video.Video;
import com.tcs.admin.catalog.domain.video.VideoGateway;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase {

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;

    public DefaultCreateVideoUseCase(
            final VideoGateway videoGateway,
            final MediaResourceGateway mediaResourceGateway,
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway,
            final CastMemberGateway castMemberGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateVideoOutput execute(final CreateVideoCommand aCommand) {
        final var aRating = Rating.of(aCommand.rating()).orElseThrow(invalidRating(aCommand.rating()));
        final var aLaunchYear = Year.of(aCommand.launchedAt());
        final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
        final var genres = toIdentifier(aCommand.genres(), GenreID::from);
        final var castMembers = toIdentifier(aCommand.castMembers(), CastMemberID::from);

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateCastMembers(castMembers));

        final var aVideo = Video.newVideo(
                aCommand.title(),
                aCommand.description(),
                aLaunchYear,
                aCommand.duration(),
                aRating,
                aCommand.opened(),
                aCommand.published(),
                categories,
                genres,
                castMembers
        );

        aVideo.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Could not create video aggregate", notification);
        }

        return CreateVideoOutput.from(create(aCommand, aVideo));
    }

    private Video create(CreateVideoCommand aCommand, Video aVideo) {
        final var anId = aVideo.getId();

        try {
            final var aVideoMedia = aCommand.getVideo()
                    .map(it -> mediaResourceGateway.storeAudioVideo(anId, it))
                    .orElse(null);

            final var aTrailerMedia = aCommand.getTrailer()
                    .map(it -> mediaResourceGateway.storeAudioVideo(anId, it))
                    .orElse(null);

            final var aBannerMedia = aCommand.getBanner()
                    .map(it -> mediaResourceGateway.storeImage(anId, it))
                    .orElse(null);

            final var aThumbMedia = aCommand.getThumbnail()
                    .map(it -> mediaResourceGateway.storeImage(anId, it))
                    .orElse(null);

            final var aThumbHalfMedia = aCommand.getThumbnailHalf()
                    .map(it -> mediaResourceGateway.storeImage(anId, it))
                    .orElse(null);

            return videoGateway.create(
                    aVideo
                            .setVideo(aVideoMedia)
                            .setTrailer(aTrailerMedia)
                            .setBanner(aBannerMedia)
                            .setThumbnail(aThumbMedia)
                            .setThumbnailHalf(aThumbHalfMedia)
            );
        } catch (final Throwable t) {
            mediaResourceGateway.clearResources(anId);
            throw InternalErrorException.with(
                    "An error occurred when creating video [videoId:%s]".formatted(anId.getValue()),
                    t
            );
        }
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids) {
        return validateAggregate("categories", ids, categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> ids) {
        return validateAggregate("genres", ids, genreGateway::existsByIds);
    }

    private ValidationHandler validateCastMembers(final Set<CastMemberID> ids) {
        return validateAggregate("cast members", ids, castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(
            final String aggregate,
            final Set<T> ids,
            final Function<Iterable<T>, List<T>> existsByIds
    ) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = existsByIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(T::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(
                    new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage))
            );
        }

        return notification;
    }

    private Supplier<DomainException> invalidRating(String rating) {
        return () -> DomainException.with(new Error("Rating not found %s".formatted(rating)));
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
        return ids.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }
}
