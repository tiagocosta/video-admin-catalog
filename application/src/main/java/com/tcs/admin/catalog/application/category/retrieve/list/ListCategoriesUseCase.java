package com.tcs.admin.catalog.application.category.retrieve.list;

import com.tcs.admin.catalog.application.UseCase;
import com.tcs.admin.catalog.domain.category.CategorySearchQuery;
import com.tcs.admin.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
