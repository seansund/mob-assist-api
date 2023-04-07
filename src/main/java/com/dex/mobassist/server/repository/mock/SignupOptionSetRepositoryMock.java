package com.dex.mobassist.server.repository.mock;

import com.dex.mobassist.server.model.SignupOptionSet;
import com.dex.mobassist.server.repository.SignupOptionSetRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository("SignupOptionSetRepository")
@Profile("db-mock")
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
