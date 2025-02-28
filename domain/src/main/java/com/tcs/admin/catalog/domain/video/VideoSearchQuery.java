package com.tcs.admin.catalog.domain.video;

import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.category.CategoryID;
import com.tcs.admin.catalog.domain.genre.GenreID;

import java.util.Set;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        Set<CategoryID> categories,
        Set<GenreID> genres,
        Set<CastMemberID> castMembers
) {
}
