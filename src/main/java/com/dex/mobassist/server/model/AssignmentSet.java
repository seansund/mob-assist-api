package com.dex.mobassist.server.model;

import java.util.List;

public interface AssignmentSet extends AssignmentSetRef {
    String getName();

    List<? extends Assignment> getAssignments();

    void setName(String name);

    void setAssignments(List<? extends Assignment> assignments);
}
