package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class AssignmentSetRef implements ModelRef {
    @NonNull
    private String id;

    public AssignmentSetRef() {
        this("");
    }

    public AssignmentSetRef(@NonNull String id) {
        this.id = id;
    }

    public static AssignmentSetRef createAssignmentSetRef(@NonNull String id) {
        return new AssignmentSetRef(id);
    }
}
