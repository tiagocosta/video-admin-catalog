package com.tcs.admin.catalog.infrastructure.configuration.usecases;

import com.tcs.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.tcs.admin.catalog.application.category.create.DefaultCreateCategoryUseCase;
import com.tcs.admin.catalog.application.category.delete.DefaultDeleteCategoryUseCase;
import com.tcs.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.tcs.admin.catalog.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.tcs.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.tcs.admin.catalog.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.tcs.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.tcs.admin.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import com.tcs.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }
}
