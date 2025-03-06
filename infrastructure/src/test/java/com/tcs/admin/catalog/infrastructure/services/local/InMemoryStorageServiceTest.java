package com.tcs.admin.catalog.infrastructure.services.local;

import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.utils.IdUtils;
import com.tcs.admin.catalog.domain.video.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryStorageServiceTest {

    private InMemoryStorageService target = new InMemoryStorageService();

    @BeforeEach
    public void setUp() {
        this.target.reset();
    }

    @Test
    public void givenValidResource_whenCallsStore_thenStoreIt(){
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(MediaType.VIDEO);

        target.store(expectedName, expectedResource);

        Assertions.assertEquals(expectedResource, target.storage().get(expectedName));
    }

    @Test
    public void givenValidResource_whenCallsGet_thenRetrieveIt(){
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(MediaType.VIDEO);

        target.storage().put(expectedName, expectedResource);

        final var actualResource = target.get(expectedName).get();

        Assertions.assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_thenRetrieveIt(){
        final var actualResource = target.get("abc");

        Assertions.assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsList_thenReturnAll(){
        final var name1 = "video_" + IdUtils.uuid();
        final var name2 = "video_" + IdUtils.uuid();
        final var name3 = "video_" + IdUtils.uuid();
        final var name4 = "image_" + IdUtils.uuid();
        final var name5 = "image_" + IdUtils.uuid();

        final var expectedNames = List.of(name1, name2, name3);
        final var all = new ArrayList<>(expectedNames);
        all.add(name4);
        all.add(name5);

        all.forEach(it -> target.storage().put(it, Fixture.Videos.resource(MediaType.VIDEO)));

        final var actualResources = target.list("video");

        Assertions.assertTrue(
                expectedNames.size() == actualResources.size()
                && expectedNames.containsAll(actualResources)
        );
    }

    @Test
    public void givenValidNames_whenCallsDeleteAll_thenDeleteAll(){
        final var name1 = "video_" + IdUtils.uuid();
        final var name2 = "video_" + IdUtils.uuid();
        final var name3 = "video_" + IdUtils.uuid();
        final var name4 = "image_" + IdUtils.uuid();
        final var name5 = "image_" + IdUtils.uuid();

        final var videos = List.of(name1, name2, name3);

        final var expectedNames = Set.of(name4, name5);

        final var all = new ArrayList<>(videos);
        all.addAll(expectedNames);

        all.forEach(it -> target.storage().put(it, Fixture.Videos.resource(MediaType.VIDEO)));

        target.deleteAll(videos);

        Assertions.assertEquals(2, target.storage().size());
        Assertions.assertTrue(
                expectedNames.size() == target.storage().size()
                        && expectedNames.containsAll(target.storage().keySet())
        );
    }
}