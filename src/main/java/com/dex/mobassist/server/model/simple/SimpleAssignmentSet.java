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
}
