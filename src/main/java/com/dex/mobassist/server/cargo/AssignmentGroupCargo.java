package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.Assignment;
import com.dex.mobassist.server.model.AssignmentGroup;
import lombok.Data;

import java.util.List;

@Data
public class AssignmentGroupCargo implements AssignmentGroup {
    private String group;
    private List<? extends Assignment> assignments;

    public AssignmentGroupCargo() {
    }

    public AssignmentGroupCargo(String group) {
        this.group = group;
    }
}
