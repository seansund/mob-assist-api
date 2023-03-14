package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.AssignmentRef;
import com.dex.mobassist.server.model.AssignmentSet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleAssignmentSet extends SimpleAssignmentSetRef implements AssignmentSet {
    private String name = "";
    private List<? extends AssignmentRef> assignments = List.of();

    public SimpleAssignmentSet() {
        this(null);
    }

    public SimpleAssignmentSet(String id) {
        super(id);
    }
}
