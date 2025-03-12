package com.tcs.admin.catalog.infrastructure.amqp;

import com.tcs.admin.catalog.application.video.media.update.UpdateMediaStatusCommand;
import com.tcs.admin.catalog.application.video.media.update.UpdateMediaStatusUseCase;
import com.tcs.admin.catalog.domain.video.MediaStatus;
import com.tcs.admin.catalog.infrastructure.configuration.json.Json;
import com.tcs.admin.catalog.infrastructure.video.models.VideoEncoderCompleted;
import com.tcs.admin.catalog.infrastructure.video.models.VideoEncoderError;
import com.tcs.admin.catalog.infrastructure.video.models.VideoEncoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VideoEncoderListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(VideoEncoderListener.class);

    public static final String LISTENER_ID = "videoEncodedListener";

    private final UpdateMediaStatusUseCase updateMediaStatusUseCase;

    public VideoEncoderListener(final UpdateMediaStatusUseCase updateMediaStatusUseCase) {
        this.updateMediaStatusUseCase = Objects.requireNonNull(updateMediaStatusUseCase);
    }

    @RabbitListener(id = LISTENER_ID, queues = "${amqp.queues.video-encoded.queue}")
    public void onVideoEncodedMessage(@Payload final String message) {
        final var aResult = Json.readValue(message, VideoEncoderResult.class);

        if (aResult instanceof VideoEncoderCompleted dto) {
            LOGGER.error("[message:video.listener.income] [status:completed] [payload:{}]", message);
            final var aCommand = UpdateMediaStatusCommand.with(
                    dto.id(),
                    dto.video().resourceId(),
                    MediaStatus.COMPLETED,
                    dto.video().encodedVideoFolder(),
                    dto.video().filePath()
            );

            this.updateMediaStatusUseCase.execute(aCommand);
        } else if (aResult instanceof VideoEncoderError dto) {
            LOGGER.error("[message:video.listener.income] [status:error] [payload:{}]", message);
        } else {
            LOGGER.error("[message:video.listener.income] [status:unknown] [payload:{}]", message);
        }
    }
}
