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

    public static AssignmentRef createAssignmentRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleAssignmentRef(id);
    }

    public static List<? extends AssignmentRef> createAssignmentRefs(@NonNull List<String> ids) {
        return ids.stream()
                .map(SimpleAssignmentRef::createAssignmentRef)
                .filter(Objects::nonNull)
                .toList();
    }
}
