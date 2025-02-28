package com.tcs.admin.catalog.infrastructure.utils;

public final class SqlUtils {

    private SqlUtils() {}

    public static String like(final String term) {
        return term == null ? null : "%" + term + "%";
    }
}
