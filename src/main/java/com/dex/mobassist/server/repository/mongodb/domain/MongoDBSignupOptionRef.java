package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.repository.BaseModelRef;
import com.dex.mobassist.server.model.SignupOptionRef;
import lombok.NonNull;

public class MongoDBSignupOptionRef extends BaseModelRef implements SignupOptionRef {
    public MongoDBSignupOptionRef() {
        this(null);
    }

    public MongoDBSignupOptionRef(String id) {
        super(id);
    }
}
