package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Assignment;

import java.util.List;

public interface AssignmentRepository extends BaseRepository<Assignment> {
    List<? extends Assignment> findAllById(List<String> assignmentIds);
}
