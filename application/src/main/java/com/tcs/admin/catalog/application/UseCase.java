package com.tcs.admin.catalog.application;

import com.tcs.admin.catalog.domain.category.Category;

public class UseCase {

    public Category execute() {
        return Category.newCategory("Drama", "Drama movies", true);
    }

}