package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Assignment extends AssignmentRef {
    private String group;
    private String name;
    private int row;

    public Assignment() {
        this("");
    }

    public Assignment(String id) {
        super(id);
    }

    public static Assignment createAssignment(@NonNull String group, @NonNull String name, int row) {
        return createAssignment(group + "-" + name, group, name, row);
    }

    public static Assignment createAssignment(@NonNull String id, @NonNull String group, @NonNull String name, int row) {
        final Assignment assignment = new Assignment(id);

        assignment.group = group;
        assignment.name = name;
        assignment.row = row;

        return assignment;
    }

    public Assignment withId(String id) {
        setId(id);

        return this;
    }

    public Assignment withId(int id) {
        return withId(String.valueOf(id));
    }
}
