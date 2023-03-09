package com.dex.mobassist.server.service.mock;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.MemberSignupResponse;
import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupOption;
import com.dex.mobassist.server.repository.MemberSignupResponseRepository;
import com.dex.mobassist.server.service.MemberSignupResponseService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MemberSignupResponseService")
@Profile("mock")
public class MemberSignupResponseServiceMock implements MemberSignupResponseService {
    private final MemberSignupResponseRepository repository;

    public MemberSignupResponseServiceMock(MemberSignupResponseRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<? extends MemberSignupResponse> list() {
        return repository.list();
    }

    @Override
    public MemberSignupResponse getById(String id) {
        return repository.getById(id);
    }

    @Override
    public MemberSignupResponse addUpdate(@NonNull MemberSignupResponse newMember) {
        return repository.addUpdate(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.delete(id);
    }

    @Override
    public Observable<List<? extends MemberSignupResponse>> observable() {
        return repository.observable();
    }

    @Override
    public List<? extends MemberSignupResponse> listByUser(String phone) {
        return repository.listByUser(phone);
    }

    @Override
    public List<? extends MemberSignupResponse> listBySignup(String id) {
        return repository.listBySignup(id);
    }

    @Override
    public Observable<List<? extends MemberSignupResponse>> observableOfUserResponses(String phone) {
        return repository.observableOfUserResponses(phone);
    }

    @Override
    public Observable<List<? extends MemberSignupResponse>> observableOfSignupResponses(String id) {
        return repository.observableOfSignupResponses(id);
    }

    @Override
    public MemberSignupResponse checkIn(String id) {
        return repository.checkIn(id);
    }

    @Override
    public MemberSignupResponse removeCheckIn(String id) {
        return repository.removeCheckIn(id);
    }

    @Override
    public MemberSignupResponse signUp(Signup signup, Member member, SignupOption option) {
        return repository.signUp(signup, member, option);
    }
}
