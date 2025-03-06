package com.tcs.admin.catalog.infrastructure.configuration.properties.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class StorageProperties implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageProperties.class);

    private String filenamePattern;

    private String locationPattern;

    public StorageProperties() {
    }

    public String getFilenamePattern() {
        return filenamePattern;
    }

    public StorageProperties setFilenamePattern(String filenamePattern) {
        this.filenamePattern = filenamePattern;
        return this;
    }

    public String getLocationPattern() {
        return locationPattern;
    }

    public StorageProperties setLocationPattern(String locationPattern) {
        this.locationPattern = locationPattern;
        return this;
    }

    @Override
    public void afterPropertiesSet() {
        LOGGER.debug(toString());
    }

    @Override
    public String toString() {
        return "StorageProperties{" +
                "filenamePattern='" + filenamePattern + '\'' +
                ", locationPattern='" + locationPattern + '\'' +
                '}';
    }
}
