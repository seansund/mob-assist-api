package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.*;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;
import java.util.Optional;

public interface MemberSignupResponseRepository extends BaseRepository<MemberSignupResponse> {
    List<? extends MemberSignupResponse> listByUser(String phone);

    List<? extends MemberSignupResponse> listBySignup(String id);

    Optional<? extends MemberSignupResponse> findForSignupAndMember(String id, String phone);

    Observable<List<? extends MemberSignupResponse>> observableOfUserResponses(String phone);

    Observable<List<? extends MemberSignupResponse>> observableOfSignupResponses(String id);

    default Optional<? extends MemberSignupResponse> checkIn(String id) {
        final Optional<? extends MemberSignupResponse> response = findById(id);

        response.ifPresent((resp) -> {
            resp.setCheckedIn(true);

            save(resp);
        });

        return response;
    }

    default Optional<? extends MemberSignupResponse> removeCheckIn(String id) {
        final Optional<? extends MemberSignupResponse> response = findById(id);

        response.ifPresent((resp) -> {
            resp.setCheckedIn(false);

            save(resp);
        });

        return response;
    }

    MemberSignupResponse signUp(Signup signup, Member member, SignupOption option);
}
