package com.tcs.admin.catalog.infrastructure;

import com.tcs.admin.catalog.application.UseCase;
import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.tcs.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}