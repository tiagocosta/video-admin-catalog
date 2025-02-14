package com.tcs.admin.catalog.application;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.tcs.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIT {

    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void cleanUp() {
        this.categoryRepository.deleteAll();
    }

    @Test
    public void testInject() {
        Assertions.assertNotNull(createCategoryUseCase);
        Assertions.assertNotNull(categoryRepository);
    }
}
