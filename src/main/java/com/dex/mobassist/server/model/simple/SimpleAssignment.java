package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.Assignment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleAssignment extends SimpleAssignmentRef implements Assignment {
    private String group;
    private String name;
    private int row;

    public SimpleAssignment() {
        this("");
    }

    public SimpleAssignment(String id) {
        super(id);
    }
}
