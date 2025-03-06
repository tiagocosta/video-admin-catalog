package com.tcs.admin.catalog.infrastructure.configuration;

import com.google.cloud.storage.Storage;
import com.tcs.admin.catalog.infrastructure.configuration.properties.google.GoogleCloudStorageProperties;
import com.tcs.admin.catalog.infrastructure.configuration.properties.storage.StorageProperties;
import com.tcs.admin.catalog.infrastructure.services.StorageService;
import com.tcs.admin.catalog.infrastructure.services.impl.GCStorageService;
import com.tcs.admin.catalog.infrastructure.services.local.InMemoryStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean
    @ConfigurationProperties(value = "storage.video-catalog")
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

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
