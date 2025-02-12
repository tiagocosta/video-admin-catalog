package com.tcs.admin.catalog.application;

import com.tcs.admin.catalog.domain.category.Category;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);

}