package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.AssignmentSet;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;

public interface AssignmentSetService extends BaseService<AssignmentSet> {
    @Override
    List<? extends AssignmentSet> list();

    @Override
    AssignmentSet getById(String id);

    @Override
    AssignmentSet addUpdate(@NonNull AssignmentSet newMember);

    @Override
    boolean delete(@NonNull String id);

    @Override
    Observable<List<? extends AssignmentSet>> observable();
}
