package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.*;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface MemberSignupResponseService extends BaseService<MemberSignupResponse> {

    List<? extends MemberSignupResponse> listByUser(String phone, SignupQueryScope scope);

    List<? extends MemberSignupResponse> listBySignup(String id);
    List<? extends MemberSignupResponse> listBySignup(String id, boolean fill);

    Optional<? extends MemberSignupResponse> getSignupResponseForUser(String signupId, String phone);

    Observable<List<? extends MemberSignupResponse>> observableOfUserResponses(String phone);

    Observable<List<? extends MemberSignupResponse>> observableOfSignupResponses(String id);

    MemberSignupResponse checkIn(String id);
    MemberSignupResponse removeCheckIn(String id);

    MemberSignupResponse signUp(Signup signup, Member member, SignupOption option);

    MemberSignupResponse signUp(Signup signup, String memberPhone, String optionValue);

}
