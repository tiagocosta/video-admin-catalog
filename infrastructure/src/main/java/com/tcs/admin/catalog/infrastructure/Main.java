package com.tcs.admin.catalog.infrastructure;

import com.tcs.admin.catalog.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

//    @RabbitListener(queues = "video.encoded.queue")
//    void dummyListener() {}
}