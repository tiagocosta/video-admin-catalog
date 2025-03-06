package com.tcs.admin.catalog.infrastructure.services.impl;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.resource.Resource;
import com.tcs.admin.catalog.domain.utils.IdUtils;
import com.tcs.admin.catalog.domain.video.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GCStorageServiceTest {

    private GCStorageService taget;

    private Storage storage;

    private String bucket = "tcsflix_test";

    @BeforeEach
    public void setUp() {
        this.storage = Mockito.mock(Storage.class);
        this.taget = new GCStorageService(this.bucket, this.storage);
    }

    @Test
    public void givenValidResource_whenCallsStore_thenSotreIt() {
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(MediaType.VIDEO);

        final var blob = mockBlob(expectedName, expectedResource);

        doReturn(blob)
                .when(this.storage).create(any(BlobInfo.class), any());

        this.taget.store(expectedName, expectedResource);

        final var captor = ArgumentCaptor.forClass(BlobInfo.class);

        verify(storage, times(1)).create(captor.capture(), eq(expectedResource.content()));

        final var actualBlob = captor.getValue();

        Assertions.assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        Assertions.assertEquals(expectedName, actualBlob.getBlobId().getName());
        Assertions.assertEquals(expectedName, actualBlob.getName());
        Assertions.assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
        Assertions.assertEquals(expectedResource.contentType(), actualBlob.getContentType());
    }

    @Test
    public void givenValidResource_whenCallsGet_thenRetrieveIt() {
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(MediaType.VIDEO);

        final var blob = mockBlob(expectedName, expectedResource);

        doReturn(blob)
                .when(this.storage).get(anyString(), anyString());

        final var actualResource = this.taget.get(expectedName).get();

        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        Assertions.assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_thenRetrieveIt() {
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(MediaType.VIDEO);

        doReturn(null)
                .when(this.storage).get(anyString(), anyString());

        final var actualResource = this.taget.get(expectedName);

        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        Assertions.assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsList_thenReturnAll() {
        final var expectedPrefix = "media_";

        final var expectedVideoName = expectedPrefix + IdUtils.uuid();
        final var expectedVideo = Fixture.Videos.resource(MediaType.VIDEO);
        final var expectedBannerName = expectedPrefix + IdUtils.uuid();
        final var expectedBanner = Fixture.Videos.resource(MediaType.BANNER);

        final var expectedNames = List.of(expectedVideoName, expectedBannerName);

        final var videoBlob = mockBlob(expectedVideoName, expectedVideo);
        final var bannerBlob = mockBlob(expectedBannerName, expectedBanner);

        final var page = Mockito.mock(Page.class);
        doReturn(List.of(videoBlob, bannerBlob))
                .when(page).iterateAll();
        doReturn(page)
                .when(this.storage).list(anyString(), any());

        final var actualNames = this.taget.list(expectedPrefix);

        verify(storage, times(1)).list(
                eq(this.bucket),
                eq(Storage.BlobListOption.prefix(expectedPrefix))
        );

        Assertions.assertTrue(
                expectedNames.size() == actualNames.size()
                        && expectedNames.containsAll(actualNames)
        );
    }

    @Test
    public void givenValidNames_whenCallsDeleteAll_thenDeleteAll() {
        final var expectedPrefix = "media_";

        final var expectedVideoName = expectedPrefix + IdUtils.uuid();
        final var expectedBannerName = expectedPrefix + IdUtils.uuid();

        final var expectedNames = List.of(expectedVideoName, expectedBannerName);

        this.taget.deleteAll(expectedNames);

        final var captor = ArgumentCaptor.forClass(List.class);

        verify(storage, times(1)).delete(captor.capture());

        final var actualNames = ((List<BlobId>) captor.getValue()).stream()
                        .map(BlobId::getName)
                                .toList();

        Assertions.assertTrue(
                expectedNames.size() == actualNames.size()
                        && expectedNames.containsAll(actualNames)
        );
    }

    private Blob mockBlob(final String name, final Resource resource) {
        final var blob = Mockito.mock(Blob.class);
        when(blob.getBlobId())
                .thenReturn(BlobId.of(this.bucket, name));
        when(blob.getCrc32cToHexString())
                .thenReturn(resource.checksum());
        when(blob.getContent())
                .thenReturn(resource.content());
        when(blob.getContentType())
                .thenReturn(resource.contentType());
        when(blob.getName())
                .thenReturn(resource.name());
        return blob;

    }
}