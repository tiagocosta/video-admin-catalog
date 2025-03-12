package com.tcs.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("COMPLETED")
public record VideoEncoderCompleted(
        String id,
        @JsonProperty("output_bucket_path") String outputBucket,
        VideoMetadata video
) implements VideoEncoderResult {

    private static final String COMPLETED = "COMPLETED";

    @Override
    public String getStatus() {
        return COMPLETED;
    }
}
