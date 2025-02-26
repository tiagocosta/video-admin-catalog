package com.tcs.admin.catalog.application.video.delete;

import com.tcs.admin.catalog.domain.video.VideoGateway;
import com.tcs.admin.catalog.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteVideoUseCaseTest {

    @Mock
    private VideoGateway videoGateway;

    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(videoGateway);
    }

    @Test
    public void givenValidId_whenCallsDeleteVideo_thenDeleteIt() {
        final var expectedId = VideoID.unique();

        doNothing()
                .when(videoGateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        verify(videoGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteVideo_thenOk() {
        final var expectedId = VideoID.from("123");

        doNothing()
                .when(videoGateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        verify(videoGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenValidId_whenCallsDeleteVideoAndGatewayThrowsException_thenReceiveException() {
        final var expectedId = VideoID.from("123");

        doThrow(new IllegalStateException("gateway error"))
                .when(videoGateway).deleteById(eq(expectedId));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> this.useCase.execute(expectedId.getValue())
        );

        verify(videoGateway).deleteById(eq(expectedId));
    }
}
