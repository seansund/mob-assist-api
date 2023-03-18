package com.dex.mobassist.server.model;

import java.util.ArrayList;
import java.util.List;

public interface AssignmentGroup {
    String getGroup();
    List<? extends Assignment> getAssignments();

    void setGroup(String group);
    void setAssignments(List<? extends Assignment> assignments);

    default <T extends AssignmentGroup> T withGroup(String group) {
        setGroup(group);

        return (T) this;
    }

    default <T extends AssignmentGroup> T withAssignments(List<? extends Assignment> assignments) {
        setAssignments(assignments);

        return (T) this;
    }

    default <T extends AssignmentGroup> T addAssignment(Assignment assignment) {
        if (assignment == null) {
            return (T) this;
        }

        final List<Assignment> newList = (getAssignments() == null)
                ? new ArrayList<>()
                : new ArrayList<>(getAssignments());

        newList.add(assignment);

        setAssignments(newList);

        return (T) this;
    }

}
