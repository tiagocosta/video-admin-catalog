package com.tcs.admin.catalog.application.castmember.update;

import com.tcs.admin.catalog.application.UseCase;
import com.tcs.admin.catalog.application.castmember.create.CreateCastMemberCommand;
import com.tcs.admin.catalog.application.castmember.create.CreateCastMemberOutput;
import com.tcs.admin.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;

public sealed abstract class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits DefaultUpdateCastMemberUseCase {
}
