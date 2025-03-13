package com.tcs.admin.catalog.infrastructure.video.presenters;

import com.tcs.admin.catalog.application.video.retrieve.get.VideoOutput;
import com.tcs.admin.catalog.application.video.update.UpdateVideoOutput;
import com.tcs.admin.catalog.domain.video.AudioVideoMedia;
import com.tcs.admin.catalog.domain.video.ImageMedia;
import com.tcs.admin.catalog.infrastructure.video.models.AudioVideoMediaResponse;
import com.tcs.admin.catalog.infrastructure.video.models.ImageMediaResponse;
import com.tcs.admin.catalog.infrastructure.video.models.UpdateVideoResponse;
import com.tcs.admin.catalog.infrastructure.video.models.VideoResponse;

public interface VideoApiPresenter {

    static VideoResponse present(final VideoOutput output) {
        return new VideoResponse(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating(),
                output.createdAt(),
                output.updatedAt(),
                present(output.banner()),
                present(output.thumbnail()),
                present(output.thumbnailHalf()),
                present(output.video()),
                present(output.trailer()),
                output.categories(),
                output.genres(),
                output.castMembers()
        );
    }

    static AudioVideoMediaResponse present(final AudioVideoMedia media) {
        if (media == null) return null;
        return new AudioVideoMediaResponse(
                media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status().name()
        );
    }

    static ImageMediaResponse present(final ImageMedia media) {
        if (media == null) return null;
        return new ImageMediaResponse(
                media.id(),
                media.checksum(),
                media.name(),
                media.location()
        );
    }

    static UpdateVideoResponse present(final UpdateVideoOutput output) {
        return new UpdateVideoResponse(output.id());
    }
}
