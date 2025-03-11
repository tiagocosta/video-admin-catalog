package com.tcs.admin.catalog.infrastructure.services;

import com.tcs.admin.catalog.domain.events.DomainEvent;

public interface EventService {

    void send(DomainEvent event);

}
