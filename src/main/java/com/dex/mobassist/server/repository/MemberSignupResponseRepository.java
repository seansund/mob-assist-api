package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.MemberSignupResponse;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

public interface MemberSignupResponseRepository extends BaseRepository<MemberSignupResponse> {
    List<MemberSignupResponse> listByUser(String phone);

    List<MemberSignupResponse> listBySignup(String id);

    Observable<List<MemberSignupResponse>> observableOfUserResponses(String phone);

    Observable<List<MemberSignupResponse>> observableOfSignupResponses(String id);

    MemberSignupResponse checkIn(String id);
    MemberSignupResponse removeCheckIn(String id);
}
