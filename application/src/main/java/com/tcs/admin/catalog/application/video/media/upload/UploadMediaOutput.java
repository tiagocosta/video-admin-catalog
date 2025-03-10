package com.tcs.admin.catalog.application.video.media.upload;

import com.tcs.admin.catalog.domain.video.MediaType;
import com.tcs.admin.catalog.domain.video.Video;

public record UploadMediaOutput(
        String videoId,
        MediaType mediaType
) {

    public static UploadMediaOutput with(final Video aVideo, final MediaType aType) {
        return new UploadMediaOutput(aVideo.getId().getValue(), aType);
    }
}
