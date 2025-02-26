package com.tcs.admin.catalog.application;

import com.tcs.admin.catalog.domain.Identifier;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class UseCaseTest {

    protected <T extends Identifier> List<String> asString(final List<T> ids) {
        return ids.stream()
                .map(T::getValue)
                .toList();
    }

    protected  <T extends Identifier> Set<String> asString(final Set<T> ids) {
        return ids.stream()
                .map(T::getValue)
                .collect(Collectors.toSet());
    }
}
