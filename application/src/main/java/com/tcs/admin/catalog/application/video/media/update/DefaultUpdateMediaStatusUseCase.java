package com.tcs.admin.catalog.application.video.media.update;

import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.video.*;

import java.util.Objects;

public class DefaultUpdateMediaStatusUseCase extends UpdateMediaStatusUseCase {

    private final VideoGateway videoGateway;

    public DefaultUpdateMediaStatusUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final UpdateMediaStatusCommand anIn) {
        final var anId = VideoID.from(anIn.videoId());
        final var resourceId = anIn.resourceId();
        final var folder = anIn.folder();
        final var filename = anIn.filename();

        final var aVideo = this.videoGateway.findById(anId)
                .orElseThrow(() -> notFound(anId));

        final var encodedPath = "%s/%s".formatted(folder, filename);
        if (matches(resourceId, aVideo.getVideo().orElse(null))) {
            updateVideo(anIn.status(), MediaType.VIDEO, aVideo, encodedPath);
        } else if (matches(resourceId, aVideo.getTrailer().orElse(null))) {
            updateVideo(anIn.status(), MediaType.TRAILER, aVideo, encodedPath);
        }
    }

    private void updateVideo(final MediaStatus aStatus, final MediaType aType, final Video aVideo, final String encodedPath) {
        switch (aStatus) {
            case PROCESSING -> this.videoGateway.update(aVideo.processing(aType));
            case COMPLETED -> this.videoGateway.update(aVideo.completed(aType, encodedPath));
            default -> {}
        }
    }

    private boolean matches(String anId, AudioVideoMedia aMedia) {
        if (aMedia == null) return false;
        return aMedia.id().equals(anId);
    }

    private NotFoundException notFound(VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }
}
