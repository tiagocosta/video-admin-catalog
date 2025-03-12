package com.tcs.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AudioVideoMediaResponse(
        String id,
        String checksum,
        String name,
        @JsonProperty("location") String rawLocation,
        String encodedLocation,
        String status
) {
}
