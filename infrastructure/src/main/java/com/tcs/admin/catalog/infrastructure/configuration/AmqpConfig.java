package com.tcs.admin.catalog.infrastructure.configuration;

import com.tcs.admin.catalog.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.tcs.admin.catalog.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.tcs.admin.catalog.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    @ConfigurationProperties("amqp.queues.video-created")
    @VideoCreatedQueue
    public QueueProperties videoCreatedQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    @ConfigurationProperties("amqp.queues.video-encoded")
    @VideoEncodedQueue
    public QueueProperties videoEncodedQueueProperties() {
        return new QueueProperties();
    }
}
