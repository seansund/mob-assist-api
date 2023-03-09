package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.Assignment;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;

public interface AssignmentService extends BaseService<Assignment> {
    abstract List<? extends Assignment> getByIds(List<String> assignmentIds);

    @Override
    List<? extends Assignment> list();

    @Override
    Assignment getById(String id);

    @Override
    Assignment addUpdate(@NonNull Assignment newMember);

    @Override
    boolean delete(@NonNull String id);

    @Override
    Observable<List<? extends Assignment>> observable();
}
