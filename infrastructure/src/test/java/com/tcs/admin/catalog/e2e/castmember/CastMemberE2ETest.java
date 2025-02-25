package com.tcs.admin.catalog.e2e.castmember;

import com.tcs.admin.catalog.E2ETest;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import com.tcs.admin.catalog.e2e.MockDsl;
import com.tcs.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.tcs.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Container
    private static final MySQLContainer<?> MYSQL_CONTAINER
            = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("videos_adm");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @BeforeEach
    void cleanUp() {
        this.castMemberRepository.deleteAll();
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToCreateNewCastMember() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.castMemberRepository.count());

        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;


        final var actualId = givenACastMember(expectedName, expectedType);

        final var actualCastMember = retrieveACastMember(actualId);

        Assertions.assertEquals(expectedName, actualCastMember.name());
        Assertions.assertEquals(expectedType.name(), actualCastMember.type());
        Assertions.assertNotNull(actualCastMember.createdAt());
        Assertions.assertNotNull(actualCastMember.updatedAt());
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToSeeTreatedErrorsByCreatingNewCastMemberWithInvalidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.castMemberRepository.count());

        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be null";

        givenACastMemberResult(expectedName, expectedType)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToNavigateAllCastMembers() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.castMemberRepository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Mel Gibson", CastMemberType.DIRECTOR);
        givenACastMember("Tarantino", CastMemberType.DIRECTOR);

        listCastMembers(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Mel Gibson")));

        listCastMembers(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Tarantino")));

        listCastMembers(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Vin Diesel")));

        listCastMembers(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(0)));
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToSearchBetweenAllCastMembers() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.castMemberRepository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Mel Gibson", CastMemberType.DIRECTOR);
        givenACastMember("Tarantino", CastMemberType.DIRECTOR);

        listCastMembers(0, 1, "tino")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Tarantino")));
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToSortAllCastMembersByNameDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.castMemberRepository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Mel Gibson", CastMemberType.DIRECTOR);
        givenACastMember("Tarantino", CastMemberType.DIRECTOR);

        listCastMembers(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Vin Diesel")))
                .andExpect(jsonPath("$.items[1].name", Matchers.equalTo("Tarantino")))
                .andExpect(jsonPath("$.items[2].name", Matchers.equalTo("Mel Gibson")));
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToGetCastMemberByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.castMemberRepository.count());

        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualId = givenACastMember(expectedName, expectedType);

        final var actualCastMember = castMemberRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertNotNull(actualCastMember.getCreatedAt());
        Assertions.assertNotNull(actualCastMember.getUpdatedAt());
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToSeeTreatedErrorByGettingNotFoundCastMember() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.castMemberRepository.count());

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        retrieveACastMemberResult(CastMemberID.from("123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToUpdateCastMemberByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.castMemberRepository.count());

        final var actualId = givenACastMember("Vind", CastMemberType.DIRECTOR);

        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var updateCastMemberRequest = new UpdateCastMemberRequest(expectedName, expectedType);

        updateACastMember(actualId, updateCastMemberRequest)
                .andExpect(status().isOk());

        final var actualCastMember = castMemberRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertNotNull(actualCastMember.getCreatedAt());
        Assertions.assertNotNull(actualCastMember.getUpdatedAt());
    }

    @Test
    public void as_aCatalogAdmin_shouldBeAbleToDeleteCastMemberByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.castMemberRepository.count());

        final var actualId = givenACastMember("Vin Diesel", CastMemberType.ACTOR);

        deleteACastMember(actualId)
                .andExpect(status().isNoContent());

        Assertions.assertFalse(castMemberRepository.existsById(actualId.getValue()));
    }
}
