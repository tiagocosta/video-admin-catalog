package com.tcs.admin.catalog.application.video.delete;

import com.tcs.admin.catalog.domain.video.MediaResourceGateway;
import com.tcs.admin.catalog.domain.video.VideoGateway;
import com.tcs.admin.catalog.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;

    private final MediaResourceGateway mediaResourceGateway;

    public DefaultDeleteVideoUseCase(
            final VideoGateway videoGateway,
            final MediaResourceGateway mediaResourceGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public void execute(final String anId) {
        final var aVideoId = VideoID.from(anId);
        this.videoGateway.deleteById(aVideoId);
        this.mediaResourceGateway.clearResources(aVideoId);
    }
}
