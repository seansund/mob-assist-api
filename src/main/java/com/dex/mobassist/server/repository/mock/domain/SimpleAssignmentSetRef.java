package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.AssignmentSetRef;
import com.dex.mobassist.server.repository.BaseModelRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleAssignmentSetRef extends BaseModelRef implements AssignmentSetRef {

    public SimpleAssignmentSetRef() {
        this(null);
    }

    public SimpleAssignmentSetRef(String id) {
        super(id);
    }
}
