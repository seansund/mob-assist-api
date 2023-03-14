package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.repository.BaseModelRef;
import com.dex.mobassist.server.model.MemberSignupResponseRef;
import lombok.NonNull;

public class MongoDBMemberSignupResponseRef extends BaseModelRef implements MemberSignupResponseRef {
    public MongoDBMemberSignupResponseRef() {
        this(null);
    }

    public MongoDBMemberSignupResponseRef(String id) {
        super(id);
    }
}
