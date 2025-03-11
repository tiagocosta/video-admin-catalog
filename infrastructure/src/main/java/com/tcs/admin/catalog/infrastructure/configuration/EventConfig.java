package com.tcs.admin.catalog.infrastructure.configuration;

import com.tcs.admin.catalog.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.tcs.admin.catalog.infrastructure.configuration.properties.amqp.QueueProperties;
import com.tcs.admin.catalog.infrastructure.services.EventService;
import com.tcs.admin.catalog.infrastructure.services.impl.RabbitEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    @VideoCreatedQueue
    public EventService videoCreatedEventService(
            @VideoCreatedQueue final QueueProperties props,
            final RabbitOperations ops
            ) {
        return new RabbitEventService(props.getExchange(), props.getRoutingKey(), ops);
    }
}
