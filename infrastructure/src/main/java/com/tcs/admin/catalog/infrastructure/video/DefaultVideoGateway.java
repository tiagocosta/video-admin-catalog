package com.tcs.admin.catalog.infrastructure.video;

import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.video.Video;
import com.tcs.admin.catalog.domain.video.VideoGateway;
import com.tcs.admin.catalog.domain.video.VideoID;
import com.tcs.admin.catalog.domain.video.VideoSearchQuery;
import com.tcs.admin.catalog.infrastructure.video.persistence.VideoJpaEntity;
import com.tcs.admin.catalog.infrastructure.video.persistence.VideoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

public class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository videoRepository;

    public DefaultVideoGateway(final VideoRepository videoRepository) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
    }

    @Override
    @Transactional
    public Video create(Video aVideo) {
        return this.videoRepository.save(VideoJpaEntity.from(aVideo))
                .toAggregate();
    }

    @Override
    public Optional<Video> findById(VideoID anId) {
        return Optional.empty();
    }

    @Override
    public void deleteById(VideoID and) {

    }

    @Override
    public Video update(Video aVideo) {
        return null;
    }

    @Override
    public Pagination<Video> findAll(VideoSearchQuery aQuery) {
        return null;
    }
}
