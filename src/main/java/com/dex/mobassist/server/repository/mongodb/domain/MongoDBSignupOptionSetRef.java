package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.repository.BaseModelRef;
import com.dex.mobassist.server.model.SignupOptionSetRef;
import lombok.NonNull;

public class MongoDBSignupOptionSetRef extends BaseModelRef implements SignupOptionSetRef {
    public MongoDBSignupOptionSetRef() {
        this(null);
    }

    public MongoDBSignupOptionSetRef(String id) {
        super(id);
    }
}
