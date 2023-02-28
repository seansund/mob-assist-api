package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.AssignmentSet;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dex.mobassist.server.model.Assignment.createAssignment;
import static com.dex.mobassist.server.model.AssignmentSet.createAssignmentSet;

@Repository("AssignmentSetRepository")
@Profile("mock")
public class AssignmentSetRepositoryMock extends AbstractRepositoryMock<AssignmentSet> implements AssignmentSetRepository {

    private int nextId = 1;

    @Override
    protected AssignmentSet generateIdForValue(AssignmentSet value) {
        return value.withId(nextId++);
    }

    @Override
    protected String getId(AssignmentSet value) {
        return value.getId();
    }
}
