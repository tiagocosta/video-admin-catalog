package com.tcs.admin.catalog.domain;

import com.tcs.admin.catalog.domain.events.DomainEvent;
import com.tcs.admin.catalog.domain.utils.IdUtils;
import com.tcs.admin.catalog.domain.utils.InstantUtils;
import com.tcs.admin.catalog.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityTest {

    @Test
    public void givenNullAsEvents_whenInstantiate_thenOk() {
        final List<DomainEvent> events = null;

        final var aDummyEntity = new DummyEntity(new DummyID(), events);

        Assertions.assertNotNull(aDummyEntity.getDomainEvents());
        Assertions.assertTrue(aDummyEntity.getDomainEvents().isEmpty());
    }

    @Test
    public void givenDomainEvents_whenInstantiateWithIt_thenCreateDefensiveClone() {
        final var expectedEventsCount = 1;
        final List<DomainEvent> events = new ArrayList<>();
        events.add(new DummyEvent());

        final var aDummyEntity = new DummyEntity(new DummyID(), events);

        Assertions.assertNotNull(aDummyEntity.getDomainEvents());
        Assertions.assertEquals(expectedEventsCount, aDummyEntity.getDomainEvents().size());

        Assertions.assertThrows(
                RuntimeException.class,
                () -> aDummyEntity.getDomainEvents().clear()
        );
    }

    @Test
    public void givenEmptyDomainEvents_whenCallsRegisterEvent_thenAddEventToList() {
        final var expectedEventsCount = 1;
        final var aDummyEntity = new DummyEntity(new DummyID(), new ArrayList<>());

        aDummyEntity.registerEvent(new DummyEvent());

        Assertions.assertNotNull(aDummyEntity.getDomainEvents());
        Assertions.assertEquals(expectedEventsCount, aDummyEntity.getDomainEvents().size());
    }

    @Test
    public void givenDomainEvents_whenCallsPublishEvents_thenCallPublisherAndClearList() {
        final var expectedEventsCount = 0;
        final var expectedSentEventsCount = 2;
        final var counter = new AtomicInteger(0);
        final var aDummyEntity = new DummyEntity(new DummyID(), new ArrayList<>());
        aDummyEntity.registerEvent(new DummyEvent());
        aDummyEntity.registerEvent(new DummyEvent());

        Assertions.assertEquals(2, aDummyEntity.getDomainEvents().size());

        aDummyEntity.publishDomainEvent(event -> {
            counter.incrementAndGet();
        });

        Assertions.assertNotNull(aDummyEntity.getDomainEvents());
        Assertions.assertEquals(expectedEventsCount, aDummyEntity.getDomainEvents().size());
        Assertions.assertEquals(expectedSentEventsCount, counter.get());
    }

    public static class DummyEvent implements DomainEvent {

        @Override
        public Instant occurredOn() {
            return InstantUtils.now();
        }
    }

    public static class DummyID extends Identifier {

        private final String id;

        public DummyID() {
            this.id = IdUtils.uuid();
        }

        @Override
        public String getValue() {
            return this.id;
        }
    }

    public static class DummyEntity extends Entity<DummyID> {

        public DummyEntity(DummyID dummyID, List<DomainEvent> domainEvents) {
            super(dummyID, domainEvents);
        }

        @Override
        public void validate(ValidationHandler handler) {

        }
    }
}
