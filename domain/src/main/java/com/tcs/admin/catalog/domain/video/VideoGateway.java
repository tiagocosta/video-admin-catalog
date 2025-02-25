package com.tcs.admin.catalog.domain.video;

import com.tcs.admin.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {

    Video create(Video aVideo);

    Optional<Video> findById(VideoID anId);

    void deleteById(VideoID and);

    Video update(Video aVideo);

    Pagination<Video> findAll(VideoSearchQuery aQuery);
}
