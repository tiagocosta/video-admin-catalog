package com.tcs.admin.catalog.infrastructure.video.presenters;

import com.tcs.admin.catalog.application.video.media.upload.UploadMediaOutput;
import com.tcs.admin.catalog.application.video.retrieve.get.VideoOutput;
import com.tcs.admin.catalog.application.video.retrieve.list.VideoListOutput;
import com.tcs.admin.catalog.application.video.update.UpdateVideoOutput;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.video.AudioVideoMedia;
import com.tcs.admin.catalog.domain.video.ImageMedia;
import com.tcs.admin.catalog.infrastructure.video.models.*;

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

    static VideoListResponse present(final VideoListOutput output) {
        return new VideoListResponse(
                output.id(),
                output.title(),
                output.description(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    static Pagination<VideoListResponse> present(final Pagination<VideoListOutput> page) {
        return page.map(VideoApiPresenter::present);
    }

    static UploadMediaResponse present(UploadMediaOutput output) {
        return new UploadMediaResponse(output.videoId(), output.mediaType());
    }
}
