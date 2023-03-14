package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.AssignmentSetRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AssignmentSetRefCargo extends BaseCargoRef implements AssignmentSetRef {
    public AssignmentSetRefCargo() {
        this(null);
    }

    public AssignmentSetRefCargo(String id) {
        super(id);
    }
}
