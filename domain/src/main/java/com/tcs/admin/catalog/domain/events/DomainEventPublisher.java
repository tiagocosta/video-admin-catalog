package com.tcs.admin.catalog.domain.events;

@FunctionalInterface
public interface DomainEventPublisher {

    void publishEvent(DomainEvent event);
}
