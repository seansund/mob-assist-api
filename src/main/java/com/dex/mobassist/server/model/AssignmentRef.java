package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class AssignmentRef implements ModelRef {
    private String id;

    public AssignmentRef() {
        this("");
    }

    public AssignmentRef(@NonNull String id) {
        this.id = id;
    }

    public static AssignmentRef createAssignmentRef(String id) {
        if (id == null) {
            return null;
        }

        return new AssignmentRef(id);
    }

    public static List<? extends AssignmentRef> createAssignmentRefs(@NonNull List<String> ids) {
        return ids.stream()
                .map(AssignmentRef::createAssignmentRef)
                .toList();
    }
}
