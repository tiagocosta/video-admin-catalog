package com.tcs.admin.catalog.domain.video;

import com.tcs.admin.catalog.domain.resource.Resource;

import java.util.Optional;

public interface MediaResourceGateway {

    Optional<Resource> getResource(VideoID anId, MediaType aType);

    AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource aResource);

    ImageMedia storeImage(VideoID anId, VideoResource aResource);

    void clearResources(VideoID anId);
}
