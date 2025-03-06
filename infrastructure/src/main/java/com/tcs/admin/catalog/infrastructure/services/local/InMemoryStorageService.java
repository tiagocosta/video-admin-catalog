package com.tcs.admin.catalog.infrastructure.services.local;

import com.tcs.admin.catalog.domain.resource.Resource;
import com.tcs.admin.catalog.infrastructure.services.StorageService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorageService implements StorageService {

    private Map<String, Resource> storage;

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }

    public Map<String, Resource> storage() {
        return this.storage;
    }

    public void reset() {
        this.storage.clear();
    }

    @Override
    public void deleteAll(final Collection<String> names) {
        names.forEach(this.storage::remove);
    }

    @Override
    public Optional<Resource> get(final String name) {
        return Optional.ofNullable(this.storage.get(name));
    }

    @Override
    public List<String> list(final String prefix) {
        if (prefix == null) {
            return Collections.emptyList();
        }
        return this.storage.keySet().stream()
                .filter(it -> it.startsWith(prefix))
                .toList();
    }

    @Override
    public void store(final String name, final Resource resource) {
        this.storage.put(name, resource);
    }
}
