package com.dex.mobassist.server.service.mock;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.repository.MemberRepository;
import com.dex.mobassist.server.service.MemberService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MemberService")
@Profile("mock")
public class MemberServiceMock implements MemberService {
    private final MemberRepository repository;

    public MemberServiceMock(MemberRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<? extends Member> list() {
        return repository.list();
    }

    @Override
    public Member getById(String id) {
        return repository.getById(id);
    }

    @Override
    public Member addUpdate(@NonNull Member newMember) {
        return repository.addUpdate(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.delete(id);
    }

    @Override
    public Observable<List<? extends Member>> observable() {
        return repository.observable();
    }
}
