package com.tcs.admin.catalog.infrastructure.configuration.properties.google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class GoogleCloudProperties implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudProperties.class);

    private String credentials;

    private String projectId;

    public String getCredentials() {
        return credentials;
    }

    public GoogleCloudProperties setCredentials(String credentials) {
        this.credentials = credentials;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public GoogleCloudProperties setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug(toString());
    }

    @Override
    public String toString() {
        return "GoogleCloudProperties{" +
                "projectId='" + projectId + '\'' +
                '}';
    }
}
