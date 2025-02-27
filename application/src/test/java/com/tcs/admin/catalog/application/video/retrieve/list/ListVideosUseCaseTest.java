package com.tcs.admin.catalog.application.video.retrieve.list;

import com.tcs.admin.catalog.application.UseCaseTest;
import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListVideosUseCaseTest extends UseCaseTest {

    @Mock
    private VideoGateway videoGateway;

    @InjectMocks
    private DefaultListVideosUseCase useCase;

    @Override
    protected void cleanUp() {
        Mockito.reset(videoGateway);
    }

    @Test
    public void givenValidQuery_whenCallsList_thenReturnVideos() {
        final var videos = List.of(
                Fixture.video(),
                Fixture.video()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedItems = videos.stream().map(VideoListOutput::from).toList();

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, videos.size(), videos);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(VideoListOutput::from);

        when(videoGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedItemsCount, actualResult.total());
        Assertions.assertEquals(expectedItems, actualResult.items());

        Mockito.verify(videoGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    public void givenValidQuery_whenHasNoVideo_thenReturnEmptyVideo() {
        final var videos = List.<Video>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, videos.size(), videos);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(VideoListOutput::from);

        when(videoGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedItemsCount, actualResult.total());
    }

    @Test
    public void givenValidQuery_whenGatewayThrowsException_thenReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "gateway error";

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(videoGateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(aQuery)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private AudioVideoMedia audioVideo(final Resource.Type type) {
        final var checksum = UUID.randomUUID().toString();
        return AudioVideoMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/media/videos/" + checksum,
                "encoded",
                MediaStatus.PENDING
        );
    }

    private ImageMedia image(final Resource.Type type) {
        final var checksum = UUID.randomUUID().toString();
        return ImageMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/media/images/" + checksum
        );
    }
}
