package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.Assignment;

import java.util.List;

public interface AssignmentService extends BaseService<Assignment> {
    public List<Assignment> getByIds(List<String> assignmentIds);
}
