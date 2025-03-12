package com.tcs.admin.catalog.infrastructure.video.models;

public record VideoMetadata(
        String encodedVideoFolder,
        String resourceId,
        String filePath
) {
}
