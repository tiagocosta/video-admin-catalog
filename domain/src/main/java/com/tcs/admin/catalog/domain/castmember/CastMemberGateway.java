package com.tcs.admin.catalog.domain.castmember;

import com.tcs.admin.catalog.domain.pagination.Pagination;
import com.tcs.admin.catalog.domain.pagination.SearchQuery;

import java.util.Optional;

public interface CastMemberGateway {

    CastMember create(CastMember aCastMember);

    void deleteById(CastMemberID anId);

    Optional<CastMember> findById(CastMemberID anId);

    CastMember update(CastMember aCastMember);

    Pagination<CastMember> findAll(SearchQuery aQuery);
}
