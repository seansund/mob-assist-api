package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.SignupOptionRef;
import com.dex.mobassist.server.repository.BaseModelRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSignupOptionRef extends BaseModelRef implements SignupOptionRef {

    public SimpleSignupOptionRef() {
        this(null);
    }

    public SimpleSignupOptionRef(String id) {
        super(id);
    }
}
