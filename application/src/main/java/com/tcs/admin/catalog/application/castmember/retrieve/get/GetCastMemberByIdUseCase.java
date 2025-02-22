package com.tcs.admin.catalog.application.castmember.retrieve.get;

import com.tcs.admin.catalog.application.UseCase;
import com.tcs.admin.catalog.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.tcs.admin.catalog.application.castmember.update.UpdateCastMemberCommand;
import com.tcs.admin.catalog.application.castmember.update.UpdateCastMemberOutput;

public sealed abstract class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase {
}
