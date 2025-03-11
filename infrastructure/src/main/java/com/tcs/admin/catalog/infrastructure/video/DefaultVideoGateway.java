package com.tcs.admin.catalog.infrastructure.video;

import com.tcs.admin.catalog.domain.Identifier;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.video.*;
import com.tcs.admin.catalog.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.tcs.admin.catalog.infrastructure.services.EventService;
import com.tcs.admin.catalog.infrastructure.video.persistence.VideoJpaEntity;
import com.tcs.admin.catalog.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.tcs.admin.catalog.domain.utils.CollectionUtils.mapTo;
import static com.tcs.admin.catalog.domain.utils.CollectionUtils.nullIfEmpty;
import static com.tcs.admin.catalog.infrastructure.utils.SqlUtils.like;
import static com.tcs.admin.catalog.infrastructure.utils.SqlUtils.upper;

@Component
public class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository videoRepository;

    private final EventService eventService;

    public DefaultVideoGateway(
            final VideoRepository videoRepository,
            @VideoCreatedQueue final EventService eventService
    ) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
        this.eventService = Objects.requireNonNull(eventService);
    }

    @Override
    @Transactional
    public Video create(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(final VideoID anId) {
        return this.videoRepository.findById(anId.getValue())
                .map(VideoJpaEntity::toDomain);
    }

    @Override
    public void deleteById(final VideoID anId) {
        final var id = anId.getValue();
        if (this.videoRepository.existsById(id)) {
            this.videoRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public Video update(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    public Pagination<VideoPreview> findAll(VideoSearchQuery aQuery) {
        final var aPage = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = this.videoRepository.findAll(
                like(upper(aQuery.terms())),
                nullIfEmpty(mapTo(aQuery.categories(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.genres(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.castMembers(), Identifier::getValue)),
                aPage
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    private Video save(final Video aVideo) {
        final var result  = this.videoRepository.save(VideoJpaEntity.from(aVideo))
                .toDomain();

        aVideo.publishDomainEvent(this.eventService::send);

        return result;
    }
}
