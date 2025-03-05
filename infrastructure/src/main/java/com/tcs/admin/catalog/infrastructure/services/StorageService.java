package com.tcs.admin.catalog.infrastructure.services;

import com.tcs.admin.catalog.domain.video.Resource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StorageService {

    void deleteAll(Collection<String> names);

    Optional<Resource> get(String name);

    List<String> list(String prefix);

    void store(String name, Resource resource);

}
