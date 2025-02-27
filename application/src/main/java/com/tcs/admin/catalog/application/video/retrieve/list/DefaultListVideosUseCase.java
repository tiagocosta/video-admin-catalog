package com.tcs.admin.catalog.application.video.retrieve.list;

import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.video.VideoGateway;
import com.tcs.admin.catalog.domain.video.VideoSearchQuery;

import java.util.Objects;

public class DefaultListVideosUseCase extends ListVideosUseCase {

    private final VideoGateway videoGateway;

    public DefaultListVideosUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<VideoListOutput> execute(VideoSearchQuery aQuery) {
        return this.videoGateway.findAll(aQuery)
                .map(VideoListOutput::from);
    }
}
