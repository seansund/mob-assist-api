package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.SignupOption;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository("SignupOptionRepository")
@Profile("mock")
public class SignupOptionRepositoryMock extends AbstractRepositoryMock<SignupOption> implements SignupOptionRepository {

    @Override
    protected SignupOption updateValueWithId(SignupOption value, int id) {
        return value.withId(id);
    }

    @Override
    protected String getId(SignupOption value) {
        return value.getId();
    }
}
