package com.dex.mobassist.server.repository.mock;

import com.dex.mobassist.server.model.Assignment;
import com.dex.mobassist.server.repository.AssignmentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("AssignmentRepository")
@Profile("mock")
public class AssignmentRepositoryMock extends AbstractRepositoryMock<Assignment> implements AssignmentRepository {
    @Override
    protected Assignment updateValueWithId(Assignment value, int id) {
        return value.withId(id);
    }

    @Override
    protected String getId(Assignment value) {
        return value.getId();
    }

    @Override
    public List<Assignment> getByIds(List<String> assignmentIds) {
        return list()
                .stream()
                .filter((Assignment assignment) -> assignmentIds.contains(assignment.getId()))
                .toList();
    }
}
