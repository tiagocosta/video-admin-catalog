package com.tcs.admin.catalog.infrastructure.configuration.usecases;

import com.tcs.admin.catalog.application.video.create.CreateVideoUseCase;
import com.tcs.admin.catalog.application.video.create.DefaultCreateVideoUseCase;
import com.tcs.admin.catalog.application.video.delete.DefaultDeleteVideoUseCase;
import com.tcs.admin.catalog.application.video.delete.DeleteVideoUseCase;
import com.tcs.admin.catalog.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.tcs.admin.catalog.application.video.media.update.UpdateMediaStatusUseCase;
import com.tcs.admin.catalog.application.video.retrieve.get.DefaultGetVideoByIdUseCase;
import com.tcs.admin.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.tcs.admin.catalog.application.video.retrieve.list.DefaultListVideosUseCase;
import com.tcs.admin.catalog.application.video.retrieve.list.ListVideosUseCase;
import com.tcs.admin.catalog.application.video.update.DefaultUpdateVideoUseCase;
import com.tcs.admin.catalog.application.video.update.UpdateVideoUseCase;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.category.CategoryGateway;
import com.tcs.admin.catalog.domain.genre.GenreGateway;
import com.tcs.admin.catalog.domain.video.MediaResourceGateway;
import com.tcs.admin.catalog.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoUseCaseConfig {

    private final VideoGateway videoGateway;

    private final MediaResourceGateway mediaResourceGateway;

    private final CategoryGateway categoryGateway;

    private final GenreGateway genreGateway;

    private final CastMemberGateway castMemberGateway;

    public VideoUseCaseConfig(
            final VideoGateway videoGateway,
            final MediaResourceGateway mediaResourceGateway,
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway,
            final CastMemberGateway castMemberGateway
    ) {
        this.videoGateway = videoGateway;
        this.mediaResourceGateway = mediaResourceGateway;
        this.categoryGateway = categoryGateway;
        this.genreGateway = genreGateway;
        this.castMemberGateway = castMemberGateway;
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(
                videoGateway,
                mediaResourceGateway,
                categoryGateway,
                genreGateway,
                castMemberGateway
        );
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(
                videoGateway,
                mediaResourceGateway,
                categoryGateway,
                genreGateway,
                castMemberGateway
        );
    }

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new DefaultGetVideoByIdUseCase(videoGateway);
    }

    @Bean
    public ListVideosUseCase listVideosUseCase() {
        return new DefaultListVideosUseCase(videoGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }
}
