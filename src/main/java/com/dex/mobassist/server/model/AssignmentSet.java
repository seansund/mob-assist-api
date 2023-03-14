package com.dex.mobassist.server.model;

import java.util.List;
import java.util.Objects;

public interface AssignmentSet extends AssignmentSetRef {
    String getName();

    List<? extends AssignmentRef> getAssignments();

    void setName(String name);

    void setAssignments(List<? extends AssignmentRef> assignments);

    default <T extends AssignmentSet> T withName(String name) {
        if (Objects.nonNull(name)) {
            setName(name);
        }

        return (T) this;
    }

    default <T extends AssignmentSet> T withAssignments(List<? extends AssignmentRef> assignments) {
        if (Objects.nonNull(assignments)) {
            setAssignments(assignments);
        }

        return (T) this;
    }
}
