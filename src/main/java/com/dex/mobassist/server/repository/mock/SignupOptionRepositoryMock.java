package com.dex.mobassist.server.repository.mock;

import com.dex.mobassist.server.model.Assignment;
import com.dex.mobassist.server.model.SignupOption;
import com.dex.mobassist.server.repository.SignupOptionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("SignupOptionRepository")
@Profile("db-mock")
public class SignupOptionRepositoryMock extends AbstractRepositoryMock<SignupOption> implements SignupOptionRepository {

    @Override
    protected SignupOption updateValueWithId(SignupOption value, int id) {
        return value.withId(id);
    }

    @Override
    protected String getId(SignupOption value) {
        return value.getId();
    }

    @Override
    public List<? extends SignupOption> findAllById(List<String> optionIds) {
        return findAll()
                .stream()
                .filter((SignupOption option) -> optionIds.contains(option.getId()))
                .toList();
    }
}
