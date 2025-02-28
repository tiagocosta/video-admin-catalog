package com.tcs.admin.catalog.application.video.retrieve.list;

import com.tcs.admin.catalog.domain.video.VideoPreview;

import java.time.Instant;

public record VideoListOutput(
        String id,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt
) {

    public static VideoListOutput from(final VideoPreview aVideoPreview) {
        return new VideoListOutput(
                aVideoPreview.id(),
                aVideoPreview.title(),
                aVideoPreview.description(),
                aVideoPreview.createdAt(),
                aVideoPreview.updateAt()
        );
    }
}
