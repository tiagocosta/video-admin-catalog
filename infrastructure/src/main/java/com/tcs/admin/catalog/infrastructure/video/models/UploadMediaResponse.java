package com.tcs.admin.catalog.infrastructure.video.models;

import com.tcs.admin.catalog.domain.video.MediaType;

public record UploadMediaResponse(
        String videoId,
        MediaType mediaType
) {
}
