package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.AssignmentSetRef;
import com.dex.mobassist.server.model.BaseModelRef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleAssignmentSetRef extends BaseModelRef implements AssignmentSetRef {

    public SimpleAssignmentSetRef() {
        this("");
    }

    public SimpleAssignmentSetRef(@NonNull String id) {
        super(id);
    }

    public static AssignmentSetRef createAssignmentSetRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleAssignmentSetRef(id);
    }
}
