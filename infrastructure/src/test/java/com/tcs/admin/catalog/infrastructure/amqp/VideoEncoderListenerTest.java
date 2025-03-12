package com.tcs.admin.catalog.infrastructure.amqp;

import com.tcs.admin.catalog.AmqpTest;
import com.tcs.admin.catalog.application.video.media.update.UpdateMediaStatusCommand;
import com.tcs.admin.catalog.application.video.media.update.UpdateMediaStatusUseCase;
import com.tcs.admin.catalog.domain.utils.IdUtils;
import com.tcs.admin.catalog.domain.video.MediaStatus;
import com.tcs.admin.catalog.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.tcs.admin.catalog.infrastructure.configuration.json.Json;
import com.tcs.admin.catalog.infrastructure.configuration.properties.amqp.QueueProperties;
import com.tcs.admin.catalog.infrastructure.video.models.VideoEncoderCompleted;
import com.tcs.admin.catalog.infrastructure.video.models.VideoEncoderError;
import com.tcs.admin.catalog.infrastructure.video.models.VideoMessage;
import com.tcs.admin.catalog.infrastructure.video.models.VideoMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@AmqpTest
public class VideoEncoderListenerTest {

    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockitoBean
    private UpdateMediaStatusUseCase updateMediaStatusUseCase;

    @Autowired
    @VideoEncodedQueue
    private QueueProperties queueProperties;

    @Test
    public void givenErrorResult_whenCallsListener_thenProcess() throws InterruptedException {
        final var expectedError = new VideoEncoderError(
                new VideoMessage("123", "abc"),
                "video not found"
        );

        final var expectedMessage = Json.writeValueAsString(expectedError);

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData =
                harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenCompletedResult_whenCallsListener_thenCallUseCase() throws InterruptedException {
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "bucket_test";
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedEncoderVideoFolder = "folder_test";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFileName = "any.mp4";
        final var expectedVideoMetadata =
                new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFileName);

        final var aResult = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedVideoMetadata);

        final var expectedMessage = Json.writeValueAsString(aResult);

        doNothing()
                .when(updateMediaStatusUseCase).execute(any());

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData =
                harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];
        Assertions.assertEquals(expectedMessage, actualMessage);

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateMediaStatusCommand.class);

        verify(updateMediaStatusUseCase).execute(cmdCaptor.capture());

        final var actualCommand = cmdCaptor.getValue();
        Assertions.assertEquals(expectedId, actualCommand.videoId());
        Assertions.assertEquals(expectedStatus, actualCommand.status());
        Assertions.assertEquals(expectedResourceId, actualCommand.resourceId());
        Assertions.assertEquals(expectedEncoderVideoFolder, actualCommand.folder());
        Assertions.assertEquals(expectedFileName, actualCommand.filename());
    }
}
