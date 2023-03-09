package com.dex.mobassist.server.service.mock;

import com.dex.mobassist.server.model.SignupOption;
import com.dex.mobassist.server.repository.SignupOptionRepository;
import com.dex.mobassist.server.service.SignupOptionService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SignupOptionService")
@Profile("mock")
public class SignupOptionServiceMock implements SignupOptionService {
    private final SignupOptionRepository repository;

    public SignupOptionServiceMock(SignupOptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<? extends SignupOption> list() {
        return repository.list();
    }

    @Override
    public SignupOption getById(String id) {
        return repository.getById(id);
    }

    @Override
    public SignupOption addUpdate(@NonNull SignupOption newMember) {
        return repository.addUpdate(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.delete(id);
    }

    @Override
    public Observable<List<? extends SignupOption>> observable() {
        return repository.observable();
    }
}
