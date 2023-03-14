package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.SignupOptionSetRef;
import com.dex.mobassist.server.repository.BaseModelRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSignupOptionSetRef extends BaseModelRef implements SignupOptionSetRef {

    public SimpleSignupOptionSetRef() {
        this(null);
    }

    public SimpleSignupOptionSetRef(String id) {
        super(id);
    }
}
