package com.dex.mobassist.server.repository.mock;

import com.dex.mobassist.server.model.AssignmentSet;
import com.dex.mobassist.server.repository.AssignmentSetRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository("AssignmentSetRepository")
@Profile("db-mock")
public class AssignmentSetRepositoryMock extends AbstractRepositoryMock<AssignmentSet> implements AssignmentSetRepository {

    @Override
    protected AssignmentSet updateValueWithId(AssignmentSet value, int id) {
        return value.withId(id);
    }

    @Override
    protected String getId(AssignmentSet value) {
        return value.getId();
    }
}
