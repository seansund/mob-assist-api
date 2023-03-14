package com.dex.mobassist.server.service.mock;

import com.dex.mobassist.server.exceptions.CurrentSignupNotFound;
import com.dex.mobassist.server.exceptions.SignupNotFound;
import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupQueryScope;
import com.dex.mobassist.server.repository.SignupRepository;
import com.dex.mobassist.server.service.SignupService;
import com.dex.mobassist.server.util.Dates;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.dex.mobassist.server.util.Dates.daysFromToday;
import static com.dex.mobassist.server.util.Dates.yesterday;

@Service("SignupService")
public class SignupServiceMock implements SignupService {
    private final SignupRepository repository;

    public SignupServiceMock(SignupRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<? extends Signup> list() {
        return repository.findAll();
    }

    @Override
    public Signup getById(String id) {
        return repository.findById(id).orElseThrow(() -> new SignupNotFound(id));
    }

    @Override
    public Signup addUpdate(@NonNull Signup newMember) {
        return repository.save(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.deleteById(id);
    }

    @Override
    public Observable<List<? extends Signup>> observable() {
        return repository.observable();
    }

    @Override
    public List<?extends Signup> listByScope(SignupQueryScope scope) {
        return switch (scope) {
            case upcoming -> repository.findUpcomingSignups();
            case future -> repository.findFutureSignups();
            case all -> repository.findAll();
        };
    }

    @Override
    public Signup getCurrent() {
        return repository.getCurrent().orElseThrow(CurrentSignupNotFound::new);
    }
}
