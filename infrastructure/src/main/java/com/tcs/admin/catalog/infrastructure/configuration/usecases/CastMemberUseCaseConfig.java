package com.tcs.admin.catalog.infrastructure.configuration.usecases;

import com.tcs.admin.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.tcs.admin.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.tcs.admin.catalog.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.tcs.admin.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.tcs.admin.catalog.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import com.tcs.admin.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.tcs.admin.catalog.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import com.tcs.admin.catalog.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.tcs.admin.catalog.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.tcs.admin.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.tcs.admin.catalog.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = castMemberGateway;
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
        return new DefaultGetCastMemberByIdUseCase(castMemberGateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new DefaultListCastMembersUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }
}
