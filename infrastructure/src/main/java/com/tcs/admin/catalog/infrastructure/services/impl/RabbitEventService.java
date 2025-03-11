package com.tcs.admin.catalog.infrastructure.services.impl;

import com.tcs.admin.catalog.domain.events.DomainEvent;
import com.tcs.admin.catalog.infrastructure.configuration.json.Json;
import com.tcs.admin.catalog.infrastructure.services.EventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;

import java.util.Objects;

public class RabbitEventService implements EventService {

    private final String exchange;

    private final String routingKey;

    private final RabbitOperations ops;

    public RabbitEventService(
            final String exchange,
            final String routingKey,
            final RabbitOperations ops
    ) {
        this.exchange = Objects.requireNonNull(exchange);
        this.routingKey = Objects.requireNonNull(routingKey);
        this.ops = Objects.requireNonNull(ops);
    }

    @Override
    public void send(DomainEvent event) {
        this.ops.convertAndSend(this.exchange, this.routingKey, Json.writeValueAsString(event));
    }
}
