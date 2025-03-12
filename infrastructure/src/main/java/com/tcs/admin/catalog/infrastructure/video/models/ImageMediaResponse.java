package com.tcs.admin.catalog.infrastructure.video.models;

public record ImageMediaResponse(
        String id,
        String checksum,
        String name,
        String location
) {
}
