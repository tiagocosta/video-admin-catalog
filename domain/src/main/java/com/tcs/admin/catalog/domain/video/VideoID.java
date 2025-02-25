package com.tcs.admin.catalog.domain.video;

import com.tcs.admin.catalog.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {

    private final String value;

    private VideoID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static VideoID from(final UUID anId) {
        return from(anId.toString());
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId.toLowerCase());
    }

    public static VideoID unique() {
        return from(UUID.randomUUID());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final VideoID that = (VideoID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
