package com.tcs.admin.catalog.application.video.retrieve.get;

import com.tcs.admin.catalog.domain.Identifier;
import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.video.Video;
import com.tcs.admin.catalog.domain.video.VideoGateway;
import com.tcs.admin.catalog.domain.video.VideoID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase {

    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String anIn) {
        final var anId  = VideoID.from(anIn);
        return this.videoGateway.findById(anId)
                .map(VideoOutput::from)
                .orElseThrow(notFound(anId));
    }

    private static Supplier<NotFoundException> notFound(Identifier anId) {
        return () -> NotFoundException.with(Video.class, anId);
    }
}
