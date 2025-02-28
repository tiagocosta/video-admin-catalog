package com.tcs.admin.catalog.domain.utils;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtils {

    private CollectionUtils() {}

    public static <IN, OUT> List<OUT> mapTo(final List<IN> list, final Function<IN, OUT> mapper) {
        if (list == null ) return null;
        return list.stream()
                .map(mapper)
                .toList();
    }

    public static <IN, OUT> Set<OUT> mapTo(final Set<IN> set, final Function<IN, OUT> mapper) {
        if (set == null) return null;
        return set.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }

    private static <IN> boolean nullIfEmpty(List<IN> list) {
        return list == null || list.isEmpty();
    }

    private static <IN> boolean nullOrEmpty(Set<IN> set) {
        return set == null || set.isEmpty();
    }
}
