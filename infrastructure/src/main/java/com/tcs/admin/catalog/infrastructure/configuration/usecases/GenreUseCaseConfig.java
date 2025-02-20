package com.tcs.admin.catalog.infrastructure.configuration.usecases;

import com.tcs.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.tcs.admin.catalog.application.genre.create.DefaultCreateGenreUseCase;
import com.tcs.admin.catalog.application.genre.delete.DefaultDeleteGenreUseCase;
import com.tcs.admin.catalog.application.genre.delete.DeleteGenreUseCase;
import com.tcs.admin.catalog.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.tcs.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.tcs.admin.catalog.application.genre.retrieve.list.DefaultListGenresUseCase;
import com.tcs.admin.catalog.application.genre.retrieve.list.ListGenresUseCase;
import com.tcs.admin.catalog.application.genre.update.DefaultUpdateGenreUseCase;
import com.tcs.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenreUseCaseConfig {

    private final GenreGateway genreGateway;

    private final CategoryGateway categoryGateway;

    public GenreUseCaseConfig(
            final GenreGateway genreGateway,
            final CategoryGateway categoryGateway
    ) {
        this.genreGateway = genreGateway;
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase() {
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }

    @Bean
    public ListGenresUseCase listGenresUseCase() {
        return new DefaultListGenresUseCase(genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }
}
