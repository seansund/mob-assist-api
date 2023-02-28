package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class AssignmentSet extends AssignmentSetRef {
    private String name;
    private List<Assignment> assignments;

    public AssignmentSet() {
        this("");
    }

    public AssignmentSet(String id) {
        super(id);
    }

    public static AssignmentSet createAssignmentSet(@NonNull String name, @NonNull List<Assignment> assignments) {
        return createAssignmentSet(name, name, assignments);
    }

    public static AssignmentSet createAssignmentSet(int id, @NonNull String name, @NonNull List<Assignment> assignments) {
        return createAssignmentSet(String.valueOf(id), name, assignments);
    }

    public static AssignmentSet createAssignmentSet(@NonNull String id, @NonNull String name, @NonNull List<Assignment> assignments) {
        final AssignmentSet set = new AssignmentSet(id);

        set.name = name;
        set.assignments = assignments;

        return set;
    }

    public AssignmentSet withId(String id) {
        this.setId(id);

        return this;
    }

    public AssignmentSet withId(int id) {
        return withId(String.valueOf(id));
    }
}
