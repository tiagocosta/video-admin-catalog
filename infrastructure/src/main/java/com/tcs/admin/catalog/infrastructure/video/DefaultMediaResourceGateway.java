package com.tcs.admin.catalog.infrastructure.video;

import com.tcs.admin.catalog.domain.resource.Resource;
import com.tcs.admin.catalog.domain.video.*;
import com.tcs.admin.catalog.infrastructure.configuration.properties.storage.StorageProperties;
import com.tcs.admin.catalog.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {

    private final String filenamePattern;

    private final String locationPattern;

    private final StorageService storageService;

    public DefaultMediaResourceGateway(final StorageProperties props, final StorageService storageService) {
        this.filenamePattern = props.getFilenamePattern();
        this.locationPattern = props.getLocationPattern();
        this.storageService = storageService;
    }

    @Override
    public Optional<Resource> getResource(VideoID anId, MediaType aType) {
        return this.storageService.get(filepath(anId, aType));
    }

    @Override
    public AudioVideoMedia storeAudioVideo(final VideoID anId, final VideoResource aVideoResource) {
        final var filepath = filepath(anId, aVideoResource.type());
        final var aResource = aVideoResource.resource();
        store(filepath, aResource);
        return AudioVideoMedia.with(aResource.checksum(), aResource.name(), filepath);
    }

    @Override
    public ImageMedia storeImage(final VideoID anId, final VideoResource aVideoResource) {
        final var filepath = filepath(anId, aVideoResource.type());
        final var aResource = aVideoResource.resource();
        store(filepath, aResource);
        return ImageMedia.with(aResource.checksum(), aResource.name(), filepath);
    }

    @Override
    public void clearResources(final VideoID anId) {
        final var ids = this.storageService.list(folder(anId));
        this.storageService.deleteAll(ids);
    }

    private String filename(final MediaType aType) {
        return filenamePattern.replace("{type}", aType.name());
    }

    private String folder(final VideoID anId) {
        return locationPattern.replace("{videoId}", anId.getValue());
    }

    private String filepath(final VideoID anId, final MediaType aType) {
        return folder(anId)
                .concat("/")
                .concat(filename(aType));
    }

    private void store(final String filepath, final Resource aResource) {
        this.storageService.store(filepath, aResource);
    }
}
