package com.tcs.admin.catalog.domain.video;

import com.tcs.admin.catalog.domain.Identifier;
import com.tcs.admin.catalog.domain.utils.IdUtils;

import java.util.Objects;

public class VideoID extends Identifier {

    private final String value;

    private VideoID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static VideoID unique() {
        return from(IdUtils.uuid());
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId.toLowerCase());
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
