package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.SignupOption;
import com.dex.mobassist.server.model.SignupOptionSet;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Predicate;

import static com.dex.mobassist.server.model.SignupOption.createSignupOption;
import static com.dex.mobassist.server.model.SignupOptionSet.createSignupOptionSet;

@Repository("SignupOptionSetRepository")
@Profile("mock")
public class SignupOptionSetRepositoryMock extends AbstractRepositoryMock<SignupOptionSet> implements SignupOptionSetRepository {
    private int nextId = 1;

    @Override
    protected SignupOptionSet generateIdForValue(SignupOptionSet value) {
        return value.withId(nextId++);
    }

    @Override
    protected String getId(SignupOptionSet value) {
        return value.getId();
    }
}
