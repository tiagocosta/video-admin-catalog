package com.tcs.admin.catalog.infrastructure.castmember.models;

import com.tcs.admin.catalog.JacksonTest;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
public class CastMemberListResponseTest {

    @Autowired
    private JacksonTester<CastMemberListResponse> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedCreatedAt = Instant.now();

        final var aResponse = new CastMemberListResponse(
                expectedId,
                expectedName,
                expectedType.name(),
                expectedCreatedAt.toString()
        );

        final var actualJson = this.json.write(aResponse);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.type", expectedType)
                .hasJsonPathValue("$.created_at", expectedCreatedAt)
        ;
    }
}
