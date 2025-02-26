package com.tcs.admin.catalog.application.video.delete;

import com.tcs.admin.catalog.domain.video.VideoGateway;
import com.tcs.admin.catalog.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;

    public DefaultDeleteVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final String anId) {
        this.videoGateway.deleteById(VideoID.from(anId));
    }
}
