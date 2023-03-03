package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Assignment;

import java.util.List;

public interface AssignmentRepository extends BaseRepository<Assignment> {
    public List<Assignment> getByIds(List<String> assignmentIds);
}
