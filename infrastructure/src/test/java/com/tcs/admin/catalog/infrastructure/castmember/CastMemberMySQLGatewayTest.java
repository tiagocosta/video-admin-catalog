package com.tcs.admin.catalog.infrastructure.castmember;

import com.tcs.admin.catalog.MySQLGatewayTest;
import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import com.tcs.admin.catalog.domain.pagination.SearchQuery;
import com.tcs.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.tcs.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @BeforeEach
    void cleanUp() {
        this.castMemberRepository.deleteAll();
    }

    @Test
    public void givenValidCastMember_whenCallsCreate_thenReturnNewCastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var aCastMember
                = CastMember.newMember(expectedName, expectedType);

        final var actualCastMember = castMemberGateway.create(aCastMember);

        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(aCastMember.getId(), actualCastMember.getId());
        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertEquals(aCastMember.getCreatedAt(), actualCastMember.getCreatedAt());
        Assertions.assertEquals(aCastMember.getUpdatedAt(), actualCastMember.getUpdatedAt());

        final var actualEntity
                = castMemberRepository.findById(aCastMember.getId().getValue()).get();

        Assertions.assertEquals(aCastMember.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(aCastMember.getType(), actualEntity.getType());
        Assertions.assertEquals(aCastMember.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aCastMember.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    public void givenValidCastMember_whenCallUpdate_thenReturnCastMemberUpdated() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var aCastMember
                = CastMember.newMember(expectedName, CastMemberType.DIRECTOR);
        final var expectedId = aCastMember.getId();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        final var anUpdatedCastMember= CastMember.with(aCastMember)
                .update(expectedName, expectedType);

        final var actualCastMember = castMemberGateway.update(anUpdatedCastMember);

        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, actualCastMember.getId());
        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertEquals(aCastMember.getCreatedAt(), actualCastMember.getCreatedAt());
        Assertions.assertTrue(aCastMember.getUpdatedAt().isBefore(actualCastMember.getUpdatedAt()));
        Assertions.assertNotNull(actualCastMember.getCreatedAt());
        Assertions.assertNotNull(actualCastMember.getUpdatedAt());

        final var actualEntity = castMemberRepository.findById(aCastMember.getId().getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedType, actualEntity.getType());
        Assertions.assertEquals(aCastMember.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(aCastMember.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
    }

    @Test
    public void givenPrePersistedCastMemberAndValidCastMemberId_whenCallDelete_thenDeleteCastMember() {
        final var aCastMember
                = CastMember.newMember("Vind Diesel", CastMemberType.ACTOR);

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        castMemberGateway.deleteById(aCastMember.getId());

        Assertions.assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void givenInvalidCastMemberId_whenCallDelete_thenDeleteCastMember() {
        Assertions.assertEquals(0, castMemberRepository.count());

        castMemberGateway.deleteById(CastMemberID.from("invalid id"));

        Assertions.assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void givenPrePersistedCastMemberAndValidCastMemberId_whenCallFindById_thenReturnCastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var aCastMember
                = CastMember.newMember(expectedName, expectedType);

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        final var actualCastMember = castMemberGateway.findById(aCastMember.getId()).get();

        Assertions.assertEquals(aCastMember.getId(), actualCastMember.getId());
        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertEquals(aCastMember.getCreatedAt(), actualCastMember.getCreatedAt());
        Assertions.assertEquals(aCastMember.getUpdatedAt(), actualCastMember.getUpdatedAt());
    }

    @Test
    public void givenValidCastMemberIdNotStored_whenCallFindById_thenReturnEmpty() {
        final var actualCastMember
                = castMemberGateway.findById(CastMemberID.from("123"));

        Assertions.assertTrue(actualCastMember.isEmpty());
    }

    @Test
    public void givenPrePersistedCastMembers_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var member1 = CastMember.newMember("Actor 1", CastMemberType.ACTOR);
        final var member2 = CastMember.newMember("Actor 2", CastMemberType.ACTOR);
        final var member3 = CastMember.newMember("Actor 3", CastMemberType.ACTOR);

        Assertions.assertEquals(0, castMemberRepository.count());

        castMemberRepository.saveAll(List.of(
                CastMemberJpaEntity.from(member1),
                CastMemberJpaEntity.from(member2),
                CastMemberJpaEntity.from(member3)
        ));

        Assertions.assertEquals(3, castMemberRepository.count());

        final var aQuery = new SearchQuery(0,1, "", "name", "asc");
        final var actualResult = castMemberGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(member1.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenEmptyCastMembersTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        final var aQuery = new SearchQuery(0,1, "", "name", "asc");
        final var actualResult = castMemberGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var member1 = CastMember.newMember("Actor 1", CastMemberType.ACTOR);
        final var member2 = CastMember.newMember("Actor 2", CastMemberType.ACTOR);
        final var member3 = CastMember.newMember("Actor 3", CastMemberType.ACTOR);

        Assertions.assertEquals(0, castMemberRepository.count());

        castMemberRepository.saveAll(List.of(
                CastMemberJpaEntity.from(member1),
                CastMemberJpaEntity.from(member2),
                CastMemberJpaEntity.from(member3)
        ));

        Assertions.assertEquals(3, castMemberRepository.count());

        // Page 0
        var aQuery = new SearchQuery(0,1, "", "name", "asc");
        var actualResult = castMemberGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(member1.getId(), actualResult.items().get(0).getId());

        // Page 1
        expectedPage += 1;
        aQuery = new SearchQuery(1,1, "", "name", "asc");
        actualResult = castMemberGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(member2.getId(), actualResult.items().get(0).getId());

        // Page 2
        expectedPage += 1;
        aQuery = new SearchQuery(2,1, "", "name", "asc");
        actualResult = castMemberGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(member3.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCastMembersAndDieselAsTerms_whenCallsFindAllAndTermsMatchesCastMemberNAme_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var member1 = CastMember.newMember("Vin Diesel", CastMemberType.ACTOR);
        final var member2 = CastMember.newMember("Mel Gibson", CastMemberType.DIRECTOR);
        final var member3 = CastMember.newMember("Tarantino", CastMemberType.DIRECTOR);

        Assertions.assertEquals(0, castMemberRepository.count());

        castMemberRepository.saveAll(List.of(
                CastMemberJpaEntity.from(member1),
                CastMemberJpaEntity.from(member2),
                CastMemberJpaEntity.from(member3)
        ));

        Assertions.assertEquals(3, castMemberRepository.count());

        final var aQuery = new SearchQuery(0,1, "diesel", "name", "asc");
        final var actualResult = castMemberGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(member1.getId().getValue(), actualResult.items().get(0).getId().getValue());
    }
}
