package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.AssignmentRef;
import com.dex.mobassist.server.model.AssignmentSet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AssignmentSetCargo extends AssignmentSetRefCargo implements AssignmentSet {
    private String name;
    private List<? extends AssignmentRef> assignments;

    public AssignmentSetCargo() {
        this(null);
    }

    public AssignmentSetCargo(String id) {
        super(id);
    }
}
