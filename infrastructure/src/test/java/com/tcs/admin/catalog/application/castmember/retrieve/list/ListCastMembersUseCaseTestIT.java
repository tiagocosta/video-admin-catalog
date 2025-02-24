package com.tcs.admin.catalog.application.castmember.retrieve.list;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.pagination.SearchQuery;
import com.tcs.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.tcs.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

import static org.mockito.Mockito.*;

@IntegrationTest
public class ListCastMembersUseCaseTestIT {

    @Autowired
    private ListCastMembersUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(castMemberGateway);
        castMemberRepository.deleteAll();
    }

    @Test
    public void givenValidQuery_whenCallsListCastMembers_thenReturnAll() {
        final var castMembers = List.of(
                CastMember.newMember("Vin Diesel", CastMemberType.ACTOR),
                CastMember.newMember("Mel Gibson", CastMemberType.DIRECTOR)
        );

        castMemberRepository.saveAllAndFlush(
                castMembers.stream()
                        .map(CastMemberJpaEntity::from)
                        .toList()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = castMembers.stream().map(CastMemberListOutput::from).toList();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, castMembers.size(), castMembers);

        final var expectedResult = expectedPagination.map(CastMemberListOutput::from);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertTrue(
                expectedItems.size() == actualResult.items().size()
                && expectedItems.containsAll(actualResult.items())
        );

        Mockito.verify(castMemberGateway, times(1)).findAll(aQuery);
    }

    @Test
    public void givenValidQuery_whenCallsListCastMembersAndThereIsNoCastMember_thenReturnEmpty() {
        final var castMembers = List.<CastMember>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<CastMemberListOutput>of();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedItems, actualResult.items());

        Mockito.verify(castMemberGateway, times(1)).findAll(aQuery);
    }

    @Test
    public void givenValidQuery_whenGatewayThrowsException_thenReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(castMemberGateway).findAll(any());

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(aQuery)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
