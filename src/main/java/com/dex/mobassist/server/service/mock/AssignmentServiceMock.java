package com.dex.mobassist.server.service.mock;

import com.dex.mobassist.server.model.Assignment;
import com.dex.mobassist.server.repository.AssignmentRepository;
import com.dex.mobassist.server.service.AssignmentService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AssignmentService")
@Profile("mock")
public class AssignmentServiceMock implements AssignmentService {
    private final AssignmentRepository repository;

    public AssignmentServiceMock(AssignmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<? extends Assignment> getByIds(List<String> assignmentIds) {
        return repository.getByIds(assignmentIds);
    }

    @Override
    public List<? extends Assignment> list() {
        return repository.list();
    }

    @Override
    public Assignment getById(String id) {
        return repository.getById(id);
    }

    @Override
    public Assignment addUpdate(@NonNull Assignment newMember) {
        return repository.addUpdate(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.delete(id);
    }

    @Override
    public Observable<List<? extends Assignment>> observable() {
        return repository.observable();
    }
}
