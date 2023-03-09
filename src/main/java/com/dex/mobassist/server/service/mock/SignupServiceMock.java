package com.dex.mobassist.server.service.mock;

import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupQueryScope;
import com.dex.mobassist.server.repository.SignupRepository;
import com.dex.mobassist.server.service.SignupService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

@Service("SignupService")
@Profile("mock")
public class SignupServiceMock implements SignupService {
    private final SignupRepository repository;

    public SignupServiceMock(SignupRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Signup> list() {
        return repository.list();
    }

    @Override
    public Signup getById(String id) {
        return repository.getById(id);
    }

    @Override
    public Signup addUpdate(@NonNull Signup newMember) {
        return repository.addUpdate(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.delete(id);
    }

    @Override
    public Observable<List<Signup>> observable() {
        return repository.observable();
    }

    @Override
    public List<Signup> list(SignupQueryScope scope) {
        return repository.list(scope);
    }

    @Override
    public Signup getCurrent() {
        return repository.getCurrent();
    }
}
