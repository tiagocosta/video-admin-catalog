package com.tcs.admin.catalog.infrastructure.services.local;

import com.tcs.admin.catalog.domain.events.DomainEvent;
import com.tcs.admin.catalog.infrastructure.configuration.json.Json;
import com.tcs.admin.catalog.infrastructure.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryEventService.class);

    @Override
    public void send(final DomainEvent event) {
        LOGGER.info("Event was observed {}", Json.writeValueAsString(event));
    }
}
