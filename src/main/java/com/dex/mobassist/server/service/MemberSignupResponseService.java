package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.*;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;

public interface MemberSignupResponseService extends BaseService<MemberSignupResponse> {
    @Override
    List<? extends MemberSignupResponse> list();

    @Override
    MemberSignupResponse getById(String id);

    @Override
    MemberSignupResponse addUpdate(@NonNull MemberSignupResponse newMember);

    @Override
    boolean delete(@NonNull String id);

    @Override
    Observable<List<? extends MemberSignupResponse>> observable();

    List<? extends MemberSignupResponse> listByUser(String phone);

    List<? extends MemberSignupResponse> listBySignup(String id);

    Observable<List<? extends MemberSignupResponse>> observableOfUserResponses(String phone);

    Observable<List<? extends MemberSignupResponse>> observableOfSignupResponses(String id);

    MemberSignupResponse checkIn(String id);
    MemberSignupResponse removeCheckIn(String id);

    MemberSignupResponse signUp(Signup signup, Member member, SignupOption option);
}
