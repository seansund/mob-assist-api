package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.repository.BaseModelRef;
import com.dex.mobassist.server.model.MemberRef;
import lombok.NonNull;

public class MongoDBMemberRef extends BaseModelRef implements MemberRef {
    public MongoDBMemberRef() {
        this(null);
    }

    public MongoDBMemberRef(String id) {
        super(id);
    }
}
