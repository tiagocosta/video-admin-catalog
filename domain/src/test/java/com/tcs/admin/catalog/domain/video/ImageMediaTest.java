package com.tcs.admin.catalog.domain.video;

import com.tcs.admin.catalog.domain.DomainTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImageMediaTest extends DomainTest {

    @Test
    public void givenValidParams_whenCallsNewImage_thenInstantiateIt() {
        final var expectedChecksum = "abc";
        final var expectedName = "banner.png";
        final var expectedLocation = "/media/images";

        final var actualImage = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        Assertions.assertNotNull(actualImage);
        Assertions.assertEquals(expectedChecksum, actualImage.checksum());
        Assertions.assertEquals(expectedName, actualImage.name());
        Assertions.assertEquals(expectedLocation, actualImage.location());
    }

    @Test
    public void givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_thenReturnTrue() {
        final var expectedChecksum = "abc";
        final var expectedLocation = "/media/images";

        final var actualImage1 = ImageMedia.with(expectedChecksum, "random.png", expectedLocation);
        final var actualImage2 = ImageMedia.with(expectedChecksum, "simple.png", expectedLocation);

        Assertions.assertEquals(actualImage1, actualImage2);
        Assertions.assertNotSame(actualImage1, actualImage2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_thenReturnError() {
        final var expectedChecksum = "abc";
        final var expectedName = "banner.png";
        final var expectedLocation = "/media/images";

        Assertions.assertThrows(
                NullPointerException.class,
                () -> ImageMedia.with(null, expectedName, expectedLocation)
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> ImageMedia.with(expectedChecksum, null, expectedLocation)
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> ImageMedia.with(expectedChecksum, expectedName, null)
        );
    }

}