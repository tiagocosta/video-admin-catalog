package com.tcs.admin.catalog.application.castmember.delete;

import com.tcs.admin.catalog.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteCastMemberUseCase {
}
