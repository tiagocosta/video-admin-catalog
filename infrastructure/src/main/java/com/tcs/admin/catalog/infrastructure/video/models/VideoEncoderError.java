package com.tcs.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ERROR")
public record VideoEncoderError(
        VideoMessage message,
        String error
) implements VideoEncoderResult {

    private static final String ERROR = "ERROR";

    @Override
    public String getStatus() {
        return ERROR;
    }
}
