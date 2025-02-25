package com.tcs.admin.catalog.infrastructure.castmember.models;

import com.tcs.admin.catalog.JacksonTest;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class CreateCastMemberRequestTest {

    @Autowired
    private JacksonTester<CreateCastMemberRequest> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var aRequest = new CreateCastMemberRequest(expectedName, expectedType);

        final var actualJson = this.json.write(aRequest);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.type", expectedType)
        ;
    }

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var json = """
        {
            "name": "%s",
            "type": "%s"
        }
        """.formatted(
                expectedName,
                expectedType.name()
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("type", expectedType)
        ;
    }
}
