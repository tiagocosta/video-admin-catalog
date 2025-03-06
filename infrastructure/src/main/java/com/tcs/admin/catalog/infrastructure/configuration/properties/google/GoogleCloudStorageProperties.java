package com.tcs.admin.catalog.infrastructure.configuration.properties.google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class GoogleCloudStorageProperties implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudStorageProperties.class);

    private String bucket;

    private int connectTimeout;

    private int readTimeout;

    private int retryDelay;

    private int retryMaxAttempts;

    private int retryMaxDelay;

    private double retryMultiplier;

    public String getBucket() {
        return bucket;
    }

    public GoogleCloudStorageProperties setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public GoogleCloudStorageProperties setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public GoogleCloudStorageProperties setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public int getRetryDelay() {
        return retryDelay;
    }

    public GoogleCloudStorageProperties setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
        return this;
    }

    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    public GoogleCloudStorageProperties setRetryMaxAttempts(int retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
        return this;
    }

    public int getRetryMaxDelay() {
        return retryMaxDelay;
    }

    public GoogleCloudStorageProperties setRetryMaxDelay(int retryMaxDelay) {
        this.retryMaxDelay = retryMaxDelay;
        return this;
    }

    public double getRetryMultiplier() {
        return retryMultiplier;
    }

    public GoogleCloudStorageProperties setRetryMultiplier(double retryMultiplier) {
        this.retryMultiplier = retryMultiplier;
        return this;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug(toString());
    }

    @Override
    public String toString() {
        return "GoogleCloudStorageProperties{" +
                "bucket='" + bucket + '\'' +
                ", connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", retryDelay=" + retryDelay +
                ", retryMaxAttempts=" + retryMaxAttempts +
                ", retryMaxDelay=" + retryMaxDelay +
                ", retryMultiplier=" + retryMultiplier +
                '}';
    }
}
