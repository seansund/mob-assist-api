package com.dex.mobassist.server.service.base;

import com.dex.mobassist.server.exceptions.AssignmentSetNotFound;
import com.dex.mobassist.server.model.AssignmentSet;
import com.dex.mobassist.server.repository.AssignmentSetRepository;
import com.dex.mobassist.server.service.AssignmentSetService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AssignmentSetService")
public class AssignmentSetServiceBase implements AssignmentSetService {
    private final AssignmentSetRepository repository;

    public AssignmentSetServiceBase(AssignmentSetRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<? extends AssignmentSet> list() {
        return repository.findAll();
    }

    @Override
    public AssignmentSet getById(String id) {
        return repository.findById(id).orElseThrow(() -> new AssignmentSetNotFound(id));
    }

    @Override
    public AssignmentSet addUpdate(@NonNull AssignmentSet newMember) {
        return repository.save(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.deleteById(id);
    }

    @Override
    public Observable<List<? extends AssignmentSet>> observable() {
        return repository.observable();
    }
}
