package com.tcs.admin.catalog.infrastructure.castmember.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CastMemberRepository extends JpaRepository<CastMemberJpaEntity, String> {

    @Query(value = "select cm.id from CastMember cm where cm.id in :ids")
    List<String> existsByIds(@Param("ids") List<String> ids);

    Page<CastMemberJpaEntity> findAll(Specification<CastMemberJpaEntity> whereClause, Pageable page);

}
