package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.model.AssignmentRef;
import com.dex.mobassist.server.repository.BaseModelRef;
import lombok.NonNull;

public class MongoDBAssignmentRef extends BaseModelRef implements AssignmentRef {
    public MongoDBAssignmentRef() {
        this(null);
    }

    public MongoDBAssignmentRef(String id) {
        super(id);
    }
}
