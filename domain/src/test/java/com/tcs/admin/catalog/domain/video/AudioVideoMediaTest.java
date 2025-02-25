package com.tcs.admin.catalog.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AudioVideoMediaTest {

    @Test
    public void givenValidParams_whenCallsNewAudioVideo_thenInstantiateIt() {
        final var expectedChecksum = "abc";
        final var expectedName = "banner.png";
        final var expectedRawLocation = "/media/images";
        final var expectedEncodedLocation = "/media/images-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        final var actualAudioVideo = AudioVideoMedia.with(
                expectedChecksum,
                expectedName,
                expectedRawLocation,
                expectedEncodedLocation,
                expectedStatus
        );

        Assertions.assertNotNull(actualAudioVideo);
        Assertions.assertEquals(expectedChecksum, actualAudioVideo.checksum());
        Assertions.assertEquals(expectedName, actualAudioVideo.name());
        Assertions.assertEquals(expectedRawLocation, actualAudioVideo.rawLocation());
        Assertions.assertEquals(expectedEncodedLocation, actualAudioVideo.encodedLocation());
        Assertions.assertEquals(expectedStatus, actualAudioVideo.status());
    }

    @Test
    public void givenTwoAudioVideosWithSameChecksumAndLocation_whenCallsEquals_thenReturnTrue() {
        final var expectedChecksum = "abc";
        final var expectedRawLocation = "/media/images";
        final var expectedEncodedLocation = "/media/images-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        final var actualAudioVideo1 = AudioVideoMedia.with(
                expectedChecksum,
                "random.png",
                expectedRawLocation,
                expectedEncodedLocation,
                expectedStatus
        );

        final var actualAudioVideo2 = AudioVideoMedia.with(
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
        final var expectedChecksum = "abc";
        final var expectedName = "banner.png";
        final var expectedRawLocation = "/media/images";
        final var expectedEncodedLocation = "/media/images-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        Assertions.assertThrows(
                NullPointerException.class,
                () -> AudioVideoMedia.with(
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
                        expectedChecksum,
                        expectedName,
                        expectedRawLocation,
                        expectedEncodedLocation,
                        null
                )
        );
    }
}