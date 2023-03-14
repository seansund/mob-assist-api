package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.AssignmentRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AssignmentRefCargo extends BaseCargoRef implements AssignmentRef {
    public AssignmentRefCargo() {
        this(null);
    }

    public AssignmentRefCargo(String id) {
        super(id);
    }

    public static List<? extends AssignmentRef> createAssignmentRefs(List<String> assignmentIds) {
        if (assignmentIds == null) {
            return List.of();
        }

        return assignmentIds.stream().map(AssignmentRefCargo::new).toList();
    }
}
