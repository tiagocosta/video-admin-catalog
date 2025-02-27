package com.tcs.admin.catalog.domain.genre;

import com.tcs.admin.catalog.domain.Identifier;
import com.tcs.admin.catalog.domain.utils.IdUtils;

import java.util.Objects;

public class GenreID extends Identifier {

    private final String value;

    private GenreID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static GenreID unique() {
        return from(IdUtils.uuid());
    }

    public static GenreID from(final String anId) {
        return new GenreID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final GenreID that = (GenreID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
