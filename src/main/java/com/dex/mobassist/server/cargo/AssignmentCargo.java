package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.Assignment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AssignmentCargo extends AssignmentRefCargo implements Assignment {
    private String group;
    private String name;
    private Integer row;

    public AssignmentCargo() {
        this(null);
    }

    public AssignmentCargo(String id) {
        super(id);
    }
}
