package com.tcs.admin.catalog.domain.utils;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtils {

    private CollectionUtils() {}

    public static <IN, OUT> List<OUT> mapTo(final List<IN> list, final Function<IN, OUT> mapper) {
        return list.stream()
                .map(mapper)
                .toList();
    }

    public static <IN, OUT> Set<OUT> mapTo(final Set<IN> set, final Function<IN, OUT> mapper) {
        return set.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }
}
