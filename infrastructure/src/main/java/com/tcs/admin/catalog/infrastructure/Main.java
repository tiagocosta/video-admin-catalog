package com.tcs.admin.catalog.infrastructure;

import com.tcs.admin.catalog.application.UseCase;
import com.tcs.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.tcs.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.tcs.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.tcs.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.tcs.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.tcs.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.tcs.admin.catalog.infrastructure.configuration.usecases.WebServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(WebServerConfig.class, args);
    }

//    @Bean
//    @DependsOnDatabaseInitialization
//    ApplicationRunner runner(
//            @Autowired CreateCategoryUseCase createCategoryUseCase,
//            @Autowired UpdateCategoryUseCase updateCategoryUseCase,
//            @Autowired DeleteCategoryUseCase deleteCategoryUseCase,
//            @Autowired GetCategoryByIdUseCase getCategoryByIdUseCase,
//            @Autowired ListCategoriesUseCase listCategoriesUseCase
//            ) {
//        return args -> {
//
//        };
//    }
}