package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.Assignment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleAssignment extends SimpleAssignmentRef implements Assignment {
    private String group = "";
    private String name = "";
    private Integer row = 0;
    private String partnerId = "";

    public SimpleAssignment() {
        this(null);
    }

    public SimpleAssignment(String id) {
        super(id);
    }
}
