package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.MemberSignupResponse;
import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupOption;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

public interface MemberSignupResponseService extends BaseService<MemberSignupResponse> {
    List<MemberSignupResponse> listByUser(String phone);

    List<MemberSignupResponse> listBySignup(String id);

    Observable<List<MemberSignupResponse>> observableOfUserResponses(String phone);

    Observable<List<MemberSignupResponse>> observableOfSignupResponses(String id);

    MemberSignupResponse checkIn(String id);
    MemberSignupResponse removeCheckIn(String id);

    MemberSignupResponse signUp(Signup signup, Member member, SignupOption option);
}
