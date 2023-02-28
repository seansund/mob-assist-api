package com.dex.mobassist.server.model;

import lombok.Data;

@Data
public class Assignment {
    private String group;
    private String name;

    public static Assignment createAssignment(String group, String name) {
        final Assignment assignment = new Assignment();

        assignment.group = group;
        assignment.name = name;

        return assignment;
    }
}
