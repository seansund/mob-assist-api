package com.dex.mobassist.server.service.base;

import com.dex.mobassist.server.exceptions.AssignmentNotFound;
import com.dex.mobassist.server.model.Assignment;
import com.dex.mobassist.server.repository.AssignmentRepository;
import com.dex.mobassist.server.service.AssignmentService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AssignmentService")
public class AssignmentServiceBase implements AssignmentService {
    private final AssignmentRepository repository;

    public AssignmentServiceBase(AssignmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<? extends Assignment> findAllById(List<String> assignmentIds) {
        return repository.findAllById(assignmentIds);
    }

    @Override
    public List<? extends Assignment> list() {
        return repository.findAll();
    }

    @Override
    public Assignment getById(String id) {
        return repository.findById(id).orElseThrow(() -> new AssignmentNotFound(id));
    }

    @Override
    public Assignment addUpdate(@NonNull Assignment newMember) {
        return repository.save(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.deleteById(id);
    }

    @Override
    public Observable<List<? extends Assignment>> observable() {
        return repository.observable();
    }
}
