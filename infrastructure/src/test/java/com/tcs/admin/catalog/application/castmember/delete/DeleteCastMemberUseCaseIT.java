package com.tcs.admin.catalog.application.castmember.delete;

import com.tcs.admin.catalog.IntegrationTest;
import com.tcs.admin.catalog.domain.castmember.CastMember;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import com.tcs.admin.catalog.domain.castmember.CastMemberID;
import com.tcs.admin.catalog.domain.castmember.CastMemberType;
import com.tcs.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.tcs.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteCastMemberUseCaseIT {

    @Autowired
    private DeleteCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @BeforeEach
    void cleanUp(){
        this.castMemberRepository.deleteAll();
    }

    @Test
    public void givenValidId_whenCallsDeleteCastMember_thenOk() {
        final var aCastMember1 =
                CastMember.newMember("Vin Diesel", CastMemberType.ACTOR);

        final var aCastMember2 =
                CastMember.newMember("Mel Gibson", CastMemberType.ACTOR);

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember1));
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember2));

        Assertions.assertEquals(2, castMemberRepository.count());

        final var expectedId = aCastMember1.getId();

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertFalse(castMemberRepository.existsById(aCastMember1.getId().getValue()));
        Assertions.assertTrue(castMemberRepository.existsById(aCastMember2.getId().getValue()));

        Mockito.verify(castMemberGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCastMember_thenOk() {
        final var expectedId = CastMemberID.from("123");

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(castMemberGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenValidId_whenCallsDeleteCastMemberAndGatewayThrowsUnexpectedError_thenReceiveException() {
        final var expectedId = CastMemberID.from("123");

        doThrow(new IllegalStateException("gateway error"))
                .when(castMemberGateway).deleteById(any());

        final var actualException
                = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals("gateway error", actualException.getMessage());

        Mockito.verify(castMemberGateway, times(1)).deleteById(expectedId);
    }
}
