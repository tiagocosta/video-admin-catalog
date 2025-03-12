package com.tcs.admin.catalog.infrastructure.video.models;

import java.util.Set;

public record CreateVideoRequest(
        String title,
        String description,
        Integer yearLaunched,
        Double duration,
        Boolean opened,
        Boolean published,
        String rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> castMembers
) {
}
