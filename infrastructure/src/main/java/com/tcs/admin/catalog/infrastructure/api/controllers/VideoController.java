package com.tcs.admin.catalog.infrastructure.api.controllers;

import com.tcs.admin.catalog.application.video.create.CreateVideoCommand;
import com.tcs.admin.catalog.application.video.create.CreateVideoUseCase;
import com.tcs.admin.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.tcs.admin.catalog.domain.resource.Resource;
import com.tcs.admin.catalog.infrastructure.api.VideoAPI;
import com.tcs.admin.catalog.infrastructure.utils.HashUtils;
import com.tcs.admin.catalog.infrastructure.video.models.CreateVideoRequest;
import com.tcs.admin.catalog.infrastructure.video.models.VideoResponse;
import com.tcs.admin.catalog.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@RestController
public class VideoController implements VideoAPI {

    private final CreateVideoUseCase createVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;

    public VideoController(
            final CreateVideoUseCase createVideoUseCase,
            final GetVideoByIdUseCase getVideoByIdUseCase
    ) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
    }

    @Override
    public ResponseEntity<?> createFull(
            final String title,
            final String description,
            final Integer yearLaunched,
            final Double duration,
            final Boolean opened,
            final Boolean published,
            final String rating,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres,
            final MultipartFile videoFile,
            final MultipartFile trailerFile,
            final MultipartFile bannerFile,
            final MultipartFile thumbFile,
            final MultipartFile thumbHalfFile
    ) {
        final var aCommand = CreateVideoCommand.with(
                title,
                description,
                yearLaunched,
                duration,
                opened,
                published,
                rating,
                categories,
                genres,
                castMembers,
                resourceOf(videoFile),
                resourceOf(trailerFile),
                resourceOf(bannerFile),
                resourceOf(thumbFile),
                resourceOf(thumbHalfFile)
        );

        final var output = this.createVideoUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> createDraft(final CreateVideoRequest payload) {
        final var aCommand = CreateVideoCommand.with(
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );

        final var output = this.createVideoUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public VideoResponse getById(String id) {
        return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(id));
    }

    private Resource resourceOf(final MultipartFile part) {
        if (part == null) return null;

        try {
            return Resource.with(
                    HashUtils.checksum(part.getBytes()),
                    part.getBytes(),
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}

