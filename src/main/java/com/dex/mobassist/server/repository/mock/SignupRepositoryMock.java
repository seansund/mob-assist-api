package com.dex.mobassist.server.repository.mock;

import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.repository.SignupRepository;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Repository("SignupRepository")
@Profile("mock")
public class SignupRepositoryMock extends AbstractRepositoryMock<Signup> implements SignupRepository {

    @Override
    protected Signup updateValueWithId(Signup value, int id) {
        return value.withId(id);
    }

    @Override
    protected String getId(Signup value) {
        return value.getId();
    }

    @Override
    public List<? extends Signup> findSignupsAfter(Date target) {
        return super.findAll()
                .stream()
                .filter(after(target))
                .toList();
    }

    @Override
    public List<? extends Signup> findSignupsBetween(@NonNull Date start, @NonNull Date end) {
        return super.findAll()
                .stream()
                .filter(between(start, end))
                .toList();
    }

    @Override
    public Optional<? extends Signup> getCurrent() {
        return this.findAll()
                .stream()
                .findFirst();
    }

    protected Predicate<Signup> after(Date start) {
        return (Signup signup) -> signup.getDate().compareTo(start) > 0;
    }

    protected Predicate<Signup> between(@NonNull Date start, @NonNull Date end) {
        return (Signup signup) -> signup.getDate().compareTo(start) > 0 && signup.getDate().compareTo(end) < 0;
    }
}
