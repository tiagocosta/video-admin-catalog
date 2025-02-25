package com.tcs.admin.catalog.infrastructure.castmember.models;

import com.tcs.admin.catalog.JacksonTest;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
public class CastMemberResponseTest {

    @Autowired
    private JacksonTester<CastMemberResponse> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();

        final var aResponse = new CastMemberResponse(
                expectedId,
                expectedName,
                expectedType.name(),
                expectedCreatedAt.toString(),
                expectedUpdatedAt.toString()
        );

        final var actualJson = this.json.write(aResponse);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.type", expectedType)
                .hasJsonPathValue("$.created_at", expectedCreatedAt)
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt)
        ;
    }

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();

        final var json = """
        {
            "id": "%s",
            "name": "%s",
            "type": "%s",
            "created_at": "%s",
            "updated_at": "%s"
        }
        """.formatted(
                expectedId,
                expectedName,
                expectedType,
                expectedCreatedAt.toString(),
                expectedUpdatedAt.toString()
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("type", expectedType.name())
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt.toString())
                .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt.toString())
        ;
    }
}
