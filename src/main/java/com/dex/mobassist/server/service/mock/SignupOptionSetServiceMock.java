package com.dex.mobassist.server.service.mock;

import com.dex.mobassist.server.model.SignupOptionSet;
import com.dex.mobassist.server.repository.SignupOptionSetRepository;
import com.dex.mobassist.server.service.SignupOptionSetService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SignupOptionSetService")
@Profile("mock")
public class SignupOptionSetServiceMock implements SignupOptionSetService {
    private SignupOptionSetRepository repository;

    public SignupOptionSetServiceMock(SignupOptionSetRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<? extends SignupOptionSet> list() {
        return repository.list();
    }

    @Override
    public SignupOptionSet getById(String id) {
        return repository.getById(id);
    }

    @Override
    public SignupOptionSet addUpdate(@NonNull SignupOptionSet newMember) {
        return repository.addUpdate(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.delete(id);
    }

    @Override
    public Observable<List<? extends SignupOptionSet>> observable() {
        return repository.observable();
    }
}
