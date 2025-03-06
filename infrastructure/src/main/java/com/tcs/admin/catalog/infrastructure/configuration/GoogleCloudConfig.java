package com.tcs.admin.catalog.infrastructure.configuration;

import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.http.HttpTransportOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.tcs.admin.catalog.infrastructure.configuration.properties.google.GoogleCloudProperties;
import com.tcs.admin.catalog.infrastructure.configuration.properties.google.GoogleCloudStorageProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.threeten.bp.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Configuration
@Profile({"development", "production"})
public class GoogleCloudConfig {

    @Bean
    @ConfigurationProperties("google.cloud")
    public GoogleCloudProperties googleCloudProperties() {
        return new GoogleCloudProperties();
    }

    @Bean
    @ConfigurationProperties("google.cloud.storage.video-catalog")
    public GoogleCloudStorageProperties googleStorageProperties() {
        return new GoogleCloudStorageProperties();
    }

    @Bean
    public Credentials credentials(final GoogleCloudProperties props) {
        final var jsonContent =
                Base64.getDecoder().decode(props.getCredentials());

        try (final var bais = new ByteArrayInputStream(jsonContent)) {
            return GoogleCredentials.fromStream(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Storage storage(
            final Credentials credentials,
            final GoogleCloudStorageProperties gcStorageProps
    ) {
        final var transportOptions = HttpTransportOptions.newBuilder()
                .setConnectTimeout(gcStorageProps.getConnectTimeout())
                .setReadTimeout(gcStorageProps.getReadTimeout())
                .build();

        final var retrySettings = RetrySettings.newBuilder()
                .setInitialRetryDelay(Duration.ofMillis(gcStorageProps.getRetryDelay()))
                .setMaxRetryDelay(Duration.ofMillis(gcStorageProps.getRetryMaxDelay()))
                .setMaxAttempts(gcStorageProps.getRetryMaxAttempts())
                .setRetryDelayMultiplier(gcStorageProps.getRetryMultiplier())
                .build();

        final var options = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setTransportOptions(transportOptions)
                .setRetrySettings(retrySettings)
                .build();

        return options.getService();
    }
}
