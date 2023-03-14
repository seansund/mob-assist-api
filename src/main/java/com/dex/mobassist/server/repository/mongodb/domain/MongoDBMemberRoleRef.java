package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.repository.BaseModelRef;
import com.dex.mobassist.server.model.MemberRoleRef;

public class MongoDBMemberRoleRef extends BaseModelRef implements MemberRoleRef {
    public MongoDBMemberRoleRef() {
        this(null);
    }
    public MongoDBMemberRoleRef(String id) {
        super(id);
    }
}
