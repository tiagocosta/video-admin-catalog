package com.tcs.admin.catalog.application.genre.retrieve.list;

import com.tcs.admin.catalog.application.UseCase;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.pagination.SearchQuery;

public abstract class ListGenresUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
