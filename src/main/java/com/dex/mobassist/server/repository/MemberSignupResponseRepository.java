package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.*;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

public interface MemberSignupResponseRepository extends BaseRepository<MemberSignupResponse> {
    List<? extends MemberSignupResponse> listByUser(String phone);

    List<? extends MemberSignupResponse> listBySignup(String id);

    Observable<List<? extends MemberSignupResponse>> observableOfUserResponses(String phone);

    Observable<List<? extends MemberSignupResponse>> observableOfSignupResponses(String id);

    MemberSignupResponse checkIn(String id);
    MemberSignupResponse removeCheckIn(String id);

    MemberSignupResponse signUp(Signup signup, Member member, SignupOption option);
}
