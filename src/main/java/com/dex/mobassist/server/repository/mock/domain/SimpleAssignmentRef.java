package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.AssignmentRef;
import com.dex.mobassist.server.repository.BaseModelRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleAssignmentRef extends BaseModelRef implements AssignmentRef {

    public SimpleAssignmentRef() {
        this(null);
    }

    public SimpleAssignmentRef(String id) {
        super(id);
    }

}
