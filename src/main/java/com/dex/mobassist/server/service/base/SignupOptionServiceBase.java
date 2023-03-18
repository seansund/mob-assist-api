package com.dex.mobassist.server.service.base;

import com.dex.mobassist.server.exceptions.SignupOptionNotFound;
import com.dex.mobassist.server.model.SignupOption;
import com.dex.mobassist.server.repository.SignupOptionRepository;
import com.dex.mobassist.server.service.SignupOptionService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SignupOptionService")
public class SignupOptionServiceBase implements SignupOptionService {
    private final SignupOptionRepository repository;

    public SignupOptionServiceBase(SignupOptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<? extends SignupOption> list() {
        return repository.findAll();
    }

    @Override
    public SignupOption getById(String id) {
        return repository.findById(id).orElseThrow(() -> new SignupOptionNotFound(id));
    }

    @Override
    public SignupOption addUpdate(@NonNull SignupOption newMember) {
        return repository.save(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.deleteById(id);
    }

    @Override
    public Observable<List<? extends SignupOption>> observable() {
        return repository.observable();
    }

    @Override
    public List<? extends SignupOption> findAllById(List<String> optionsIds) {
        return repository.findAllById(optionsIds);
    }
}
