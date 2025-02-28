package com.tcs.admin.catalog.domain.video;

import java.time.Instant;

public record VideoPreview(
        String id,
        String title,
        String description,
        Instant updateAt
) {
}
