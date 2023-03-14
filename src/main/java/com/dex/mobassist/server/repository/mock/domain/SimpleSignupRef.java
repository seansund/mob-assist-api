package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.SignupRef;
import com.dex.mobassist.server.repository.BaseModelRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSignupRef extends BaseModelRef implements SignupRef {

    public SimpleSignupRef() {
        this(null);
    }

    public SimpleSignupRef(String id) {
        super(id);
    }
}
