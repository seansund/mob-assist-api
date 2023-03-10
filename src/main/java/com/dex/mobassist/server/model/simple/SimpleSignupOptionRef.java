package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.BaseModelRef;
import com.dex.mobassist.server.model.SignupOptionRef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSignupOptionRef extends BaseModelRef implements SignupOptionRef {

    public SimpleSignupOptionRef() {
        this("");
    }

    public SimpleSignupOptionRef(@NonNull String id) {
        super(id);
    }
}
