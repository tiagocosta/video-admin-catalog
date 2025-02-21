package com.tcs.admin.catalog.application.castmember.create;

import com.tcs.admin.catalog.application.UseCase;

public sealed abstract class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {
}
