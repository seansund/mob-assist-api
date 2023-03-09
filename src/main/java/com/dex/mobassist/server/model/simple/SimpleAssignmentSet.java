package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.Assignment;
import com.dex.mobassist.server.model.AssignmentSet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleAssignmentSet extends SimpleAssignmentSetRef implements AssignmentSet {
    private String name;
    private List<? extends Assignment> assignments;

    public SimpleAssignmentSet() {
        this("");
    }

    public SimpleAssignmentSet(String id) {
        super(id);
    }

    public static AssignmentSet createAssignmentSet(@NonNull String name, @NonNull List<? extends Assignment> assignments) {
        return createAssignmentSet(name, name, assignments);
    }

    public static AssignmentSet createAssignmentSet(int id, @NonNull String name, @NonNull List<? extends Assignment> assignments) {
        return createAssignmentSet(String.valueOf(id), name, assignments);
    }

    public static AssignmentSet createAssignmentSet(@NonNull String id, @NonNull String name, @NonNull List<? extends Assignment> assignments) {
        final SimpleAssignmentSet set = new SimpleAssignmentSet(id);

        set.name = name;
        set.assignments = assignments;

        return set;
    }
}
