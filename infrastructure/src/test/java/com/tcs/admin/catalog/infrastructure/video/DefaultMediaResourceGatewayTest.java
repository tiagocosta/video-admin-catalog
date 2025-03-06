package com.tcs.admin.catalog.infrastructure.video;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.video.*;
import com.tcs.admin.catalog.infrastructure.services.StorageService;
import com.tcs.admin.catalog.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@IntegrationTest
class DefaultMediaResourceGatewayTest {

    @Autowired
    private MediaResourceGateway mediaResourceGateway;

    @Autowired
    private StorageService storageService;

    @BeforeEach
    void setUp(){
        storageService().reset();
    }

    @Test
    public void givenValidResource_whenCallsStoreAudioVideo_thenStoreIt() {
        final var expectedVideoId = VideoID.unique();
        final var expectedType = MediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());
        final var expectedEncodedLocation = "";
        final var expectedStatus = MediaStatus.PENDING;

        final var actualMedia =
                this.mediaResourceGateway.storeAudioVideo(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        Assertions.assertNotNull(actualMedia.id());
        Assertions.assertEquals(expectedLocation, actualMedia.rawLocation());
        Assertions.assertEquals(expectedResource.name(), actualMedia.name());
        Assertions.assertEquals(expectedResource.checksum(), actualMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualMedia.status());
        Assertions.assertEquals(expectedEncodedLocation, actualMedia.encodedLocation());

        final var actualStored = storageService().storage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidResource_whenCallsStoreImage_thenStoreIt() {
        final var expectedVideoId = VideoID.unique();
        final var expectedType = MediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());

        final var actualMedia =
                this.mediaResourceGateway.storeImage(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        Assertions.assertNotNull(actualMedia.id());
        Assertions.assertEquals(expectedLocation, actualMedia.location());
        Assertions.assertEquals(expectedResource.name(), actualMedia.name());
        Assertions.assertEquals(expectedResource.checksum(), actualMedia.checksum());

        final var actualStored = storageService().storage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidVideoId_whenCallsClearResources_thenDeleteAll() {
        final var videoOne = VideoID.unique();
        final var videoTwo = VideoID.unique();

        final var toBeDeleted = new ArrayList<String>();
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), MediaType.VIDEO.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), MediaType.TRAILER.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), MediaType.BANNER.name()));

        final var expectedVideos = new ArrayList<String>();
        expectedVideos.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), MediaType.VIDEO.name()));
        expectedVideos.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), MediaType.BANNER.name()));

        toBeDeleted.forEach(id -> storageService().store(id, Fixture.Videos.resource(Fixture.mediaType())));
        expectedVideos.forEach(id -> storageService().store(id, Fixture.Videos.resource(Fixture.mediaType())));

        Assertions.assertEquals(5, storageService().storage().size());

        this.mediaResourceGateway.clearResources(videoOne);

        Assertions.assertEquals(2, storageService().storage().size());

        final var actualKeys = storageService().storage().keySet();
        Assertions.assertTrue(
                expectedVideos.size() == actualKeys.size()
                && expectedVideos.containsAll(actualKeys)
        );
    }

    private InMemoryStorageService storageService() {
        return (InMemoryStorageService) storageService;
    }
}