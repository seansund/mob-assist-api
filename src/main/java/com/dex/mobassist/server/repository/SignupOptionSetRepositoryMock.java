package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.SignupOptionSet;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository("SignupOptionSetRepository")
@Profile("mock")
public class SignupOptionSetRepositoryMock extends AbstractRepositoryMock<SignupOptionSet> implements SignupOptionSetRepository {

    @Override
    protected SignupOptionSet updateValueWithId(SignupOptionSet value, int id) {
        return value.withId(id);
    }

    @Override
    protected String getId(SignupOptionSet value) {
        return value.getId();
    }
}
