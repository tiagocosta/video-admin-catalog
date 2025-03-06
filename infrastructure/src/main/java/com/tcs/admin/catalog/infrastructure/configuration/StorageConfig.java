package com.tcs.admin.catalog.infrastructure.configuration;

import com.google.cloud.storage.Storage;
import com.tcs.admin.catalog.infrastructure.configuration.properties.GoogleCloudStorageProperties;
import com.tcs.admin.catalog.infrastructure.services.StorageService;
import com.tcs.admin.catalog.infrastructure.services.impl.GCStorageService;
import com.tcs.admin.catalog.infrastructure.services.local.InMemoryStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean(name = "storageService")
    @Profile({"development", "production"})
    public StorageService gcStorageService(final GoogleCloudStorageProperties props, final Storage storage) {
        return new GCStorageService(props.getBucket(), storage);
    }

    @Bean(name = "storageService")
    @ConditionalOnMissingBean
    public StorageService inMemoryStorageService() {
        return new InMemoryStorageService();
    }
}
