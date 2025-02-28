package com.tcs.admin.catalog.domain.video;

import com.tcs.admin.catalog.domain.utils.IdUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AudioVideoMediaTest {

    @Test
    public void givenValidParams_whenCallsNewAudioVideo_thenInstantiateIt() {
        final var expectedId = IdUtils.uuid();
        final var expectedChecksum = "abc";
        final var expectedName = "banner.png";
        final var expectedRawLocation = "/media/images";
        final var expectedEncodedLocation = "/media/images-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        final var actualAudioVideo = AudioVideoMedia.with(
                expectedId,
                expectedChecksum,
                expectedName,
                expectedRawLocation,
                expectedEncodedLocation,
                expectedStatus
        );

        Assertions.assertNotNull(actualAudioVideo);
        Assertions.assertEquals(expectedId, actualAudioVideo.id());
        Assertions.assertEquals(expectedChecksum, actualAudioVideo.checksum());
        Assertions.assertEquals(expectedName, actualAudioVideo.name());
        Assertions.assertEquals(expectedRawLocation, actualAudioVideo.rawLocation());
        Assertions.assertEquals(expectedEncodedLocation, actualAudioVideo.encodedLocation());
        Assertions.assertEquals(expectedStatus, actualAudioVideo.status());
    }

    @Test
    public void givenTwoAudioVideosWithSameChecksumAndLocation_whenCallsEquals_thenReturnTrue() {
        final var expectedId = IdUtils.uuid();
        final var expectedChecksum = "abc";
        final var expectedRawLocation = "/media/images";
        final var expectedEncodedLocation = "/media/images-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        final var actualAudioVideo1 = AudioVideoMedia.with(
                expectedId,
                expectedChecksum,
                "random.png",
                expectedRawLocation,
                expectedEncodedLocation,
                expectedStatus
        );

        final var actualAudioVideo2 = AudioVideoMedia.with(
                expectedId,
                expectedChecksum,
                "simple.png",
                expectedRawLocation,
                expectedEncodedLocation,
                expectedStatus
        );

        Assertions.assertEquals(actualAudioVideo1, actualAudioVideo2);
        Assertions.assertNotSame(actualAudioVideo1, actualAudioVideo2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_thenReturnError() {
        final var expectedId = IdUtils.uuid();
        final var expectedChecksum = "abc";
        final var expectedName = "banner.png";
        final var expectedRawLocation = "/media/images";
        final var expectedEncodedLocation = "/media/images-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        Assertions.assertThrows(
                NullPointerException.class,
                () -> AudioVideoMedia.with(
                        null,
                        expectedChecksum,
                        expectedName,
                        expectedRawLocation,
                        expectedEncodedLocation,
                        expectedStatus
                )
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> AudioVideoMedia.with(
                        expectedId,
                        null,
                        expectedName,
                        expectedRawLocation,
                        expectedEncodedLocation,
                        expectedStatus
                )
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> AudioVideoMedia.with(
                        expectedId,
                        expectedChecksum,
                        null,
                        expectedRawLocation,
                        expectedEncodedLocation,
                        expectedStatus
                )
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> AudioVideoMedia.with(
                        expectedId,
                        expectedChecksum,
                        expectedName,
                        null,
                        expectedEncodedLocation,
                        expectedStatus
                )
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> AudioVideoMedia.with(
                        expectedId,
                        expectedChecksum,
                        expectedName,
                        expectedRawLocation,
                        null,
                        expectedStatus
                )
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> AudioVideoMedia.with(
                        expectedId,
                        expectedChecksum,
                        expectedName,
                        expectedRawLocation,
                        expectedEncodedLocation,
                        null
                )
        );
    }
}