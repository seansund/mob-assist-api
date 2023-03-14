package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupQueryScope;
import com.dex.mobassist.server.util.Dates;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.dex.mobassist.server.util.Dates.daysFromToday;
import static com.dex.mobassist.server.util.Dates.yesterday;

public interface SignupRepository extends BaseRepository<Signup> {

    List<? extends Signup> findSignupsAfter(Date target);

    List<? extends Signup> findSignupsBetween(Date start, Date end);

    Optional<? extends Signup> getCurrent();

    default List<? extends Signup> findSignupsByScope(SignupQueryScope scope) {
        return switch (scope) {
            case upcoming -> findUpcomingSignups();
            case future -> findFutureSignups();
            case all -> findAll();
        };
    }

    default List<? extends Signup> findFutureSignups() {
        return findSignupsAfter(yesterday());
    }

    default List<? extends Signup> findUpcomingSignups() {
        return findSignupsBetween(yesterday(), daysFromToday(120));
    }
}
