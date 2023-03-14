package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.model.AssignmentSetRef;
import com.dex.mobassist.server.repository.BaseModelRef;
import lombok.NonNull;

public class MongoDBAssignmentSetRef extends BaseModelRef implements AssignmentSetRef {
    public MongoDBAssignmentSetRef() {
        this(null);
    }

    public MongoDBAssignmentSetRef(String id) {
        super(id);
    }
}
