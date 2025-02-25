package com.tcs.admin.catalog.domain.video;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
