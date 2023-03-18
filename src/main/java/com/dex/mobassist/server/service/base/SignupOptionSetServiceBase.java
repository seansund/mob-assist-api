package com.dex.mobassist.server.service.base;

import com.dex.mobassist.server.exceptions.SignupOptionSetNotFound;
import com.dex.mobassist.server.model.SignupOptionSet;
import com.dex.mobassist.server.repository.SignupOptionSetRepository;
import com.dex.mobassist.server.service.SignupOptionSetService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SignupOptionSetService")
public class SignupOptionSetServiceBase implements SignupOptionSetService {
    private SignupOptionSetRepository repository;

    public SignupOptionSetServiceBase(SignupOptionSetRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<? extends SignupOptionSet> list() {
        return repository.findAll();
    }

    @Override
    public SignupOptionSet getById(String id) {
        return repository.findById(id).orElseThrow(() -> new SignupOptionSetNotFound(id));
    }

    @Override
    public SignupOptionSet addUpdate(@NonNull SignupOptionSet newMember) {
        return repository.save(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.deleteById(id);
    }

    @Override
    public Observable<List<? extends SignupOptionSet>> observable() {
        return repository.observable();
    }
}
