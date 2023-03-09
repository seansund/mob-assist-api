package com.dex.mobassist.server.service.mock;

import com.dex.mobassist.server.model.AssignmentSet;
import com.dex.mobassist.server.repository.AssignmentSetRepository;
import com.dex.mobassist.server.service.AssignmentSetService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AssignmentSetService")
@Profile("mock")
public class AssignmentSetServiceMock implements AssignmentSetService {
    private final AssignmentSetRepository repository;

    public AssignmentSetServiceMock(AssignmentSetRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AssignmentSet> list() {
        return repository.list();
    }

    @Override
    public AssignmentSet getById(String id) {
        return repository.getById(id);
    }

    @Override
    public AssignmentSet addUpdate(@NonNull AssignmentSet newMember) {
        return repository.addUpdate(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.delete(id);
    }

    @Override
    public Observable<List<AssignmentSet>> observable() {
        return repository.observable();
    }
}
