package com.dex.mobassist.server.model;

import java.util.List;

public interface AssignmentSet extends AssignmentSetRef {
    String getName();

    List<? extends Assignment> getAssignments();

    void setName(String name);

    void setAssignments(List<? extends Assignment> assignments);

    default <T extends AssignmentSet> T withName(String name) {
        setName(name);

        return (T) this;
    }

    default <T extends AssignmentSet> T withAssignments(List<? extends Assignment> assignments) {
        setAssignments(assignments);

        return (T) this;
    }
}
