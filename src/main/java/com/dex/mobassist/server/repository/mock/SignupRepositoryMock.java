package com.dex.mobassist.server.repository.mock;

import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupQueryScope;
import com.dex.mobassist.server.repository.SignupRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    public List<Signup> list() {
        return list(SignupQueryScope.upcoming);
    }

    @Override
    public List<Signup> list(SignupQueryScope scope) {

        final Predicate<Signup> predicate = getPredicate(scope);

        return super.list()
                .stream()
                .filter(predicate)
                .toList();
    }

    @Override
    public Signup getCurrent() {
        return list(SignupQueryScope.upcoming)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Current signup not found"));
    }

    protected Predicate<Signup> getPredicate(SignupQueryScope scope) {
        final Date today = new Date();
        final Calendar upcoming = Calendar.getInstance();
        upcoming.add(Calendar.MONTH, 3);

        switch (scope) {
            case all -> {
                return (Signup signup) -> true;
            }
            case future -> {
                return (Signup signup) -> signup.getDate().compareTo(today) > 0;
            }
            default -> {
                return (Signup signup) -> signup.getDate().compareTo(today) > 0 && signup.getDate().compareTo(upcoming.getTime()) < 0;
            }
        }
    }
}
