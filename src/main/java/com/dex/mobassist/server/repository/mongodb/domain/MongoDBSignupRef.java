package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.repository.BaseModelRef;
import com.dex.mobassist.server.model.SignupRef;
import lombok.NonNull;

public class MongoDBSignupRef extends BaseModelRef implements SignupRef {
    public MongoDBSignupRef() {
        this(null);
    }

    public MongoDBSignupRef(String id) {
        super(id);
    }
}
