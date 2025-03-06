package com.tcs.admin.catalog.application.video.retrieve.list;

import com.tcs.admin.catalog.application.UseCaseTest;
import com.tcs.admin.catalog.domain.Fixture;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.resource.Resource;
import com.tcs.admin.catalog.domain.utils.IdUtils;
import com.tcs.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
                Fixture.videoPreview(),
                Fixture.videoPreview()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedItems = videos.stream().map(VideoListOutput::from).toList();

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        null,
                        null,
                        null
                );

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
        final var videos = List.<VideoPreview>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        null,
                        null,
                        null
                );

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
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        null,
                        null,
                        null
                );

        when(videoGateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(aQuery)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private AudioVideoMedia audioVideo(final MediaType type) {
        final var id = IdUtils.uuid();
        return AudioVideoMedia.with(
                id,
                "123",
                type.name().toLowerCase(),
                "/media/videos/123",
                "encoded",
                MediaStatus.PENDING
        );
    }

    private ImageMedia image(final MediaType type) {
        final var checksum = IdUtils.uuid();
        return ImageMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/media/images/" + checksum
        );
    }
}
