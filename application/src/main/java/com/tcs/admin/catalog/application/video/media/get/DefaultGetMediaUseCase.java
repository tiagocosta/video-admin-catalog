package com.tcs.admin.catalog.application.video.media.get;

import com.tcs.admin.catalog.domain.exceptions.NotFoundException;
import com.tcs.admin.catalog.domain.validation.Error;
import com.tcs.admin.catalog.domain.video.MediaResourceGateway;
import com.tcs.admin.catalog.domain.video.MediaType;
import com.tcs.admin.catalog.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetMediaUseCase extends GetMediaUseCase {

    private final MediaResourceGateway mediaResourceGateway;

    public DefaultGetMediaUseCase(final MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public MediaOutput execute(GetMediaCommand anIn) {
        final var anId = VideoID.from(anIn.videoId());
        final var aType = MediaType.of(anIn.mediaType())
                .orElseThrow(() -> typeNotFound(anIn.mediaType()));

        final var aResource = this.mediaResourceGateway.getResource(anId, aType)
                .orElseThrow(() -> notFound(anIn.videoId(), anIn.mediaType()));

        return MediaOutput.with(aResource);
    }

    private NotFoundException typeNotFound(String aType) {
        return NotFoundException.with(new Error("Media type %s does not exist".formatted(aType)));
    }

    private NotFoundException notFound(String anId, String aType) {
        return NotFoundException.with(new Error("Resource %s not found for video %s".formatted(aType, anId)));
    }
}
