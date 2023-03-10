package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.AssignmentRef;
import com.dex.mobassist.server.model.BaseModelRef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleAssignmentRef extends BaseModelRef implements AssignmentRef {

    public SimpleAssignmentRef() {
        this("");
    }

    public SimpleAssignmentRef(@NonNull String id) {
        super(id);
    }

}
