package com.tcs.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Set;

public record VideoResponse(
        String id,
        String title,
        String description,
        int yearLaunched,
        double duration,
        boolean opened,
        boolean published,
        String rating,
        Instant createdAt,
        Instant updatedAt,
        ImageMediaResponse banner,
        ImageMediaResponse thumbnail,
        ImageMediaResponse thumbnailHalf,
        AudioVideoMediaResponse video,
        AudioVideoMediaResponse trailer,
        @JsonProperty("categories_id") Set<String> categories,
        @JsonProperty("genres_id") Set<String> genres,
        @JsonProperty("cast_members_id") Set<String> castMembers
) {
}
