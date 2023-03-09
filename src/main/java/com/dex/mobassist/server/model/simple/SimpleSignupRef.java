package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.BaseModelRef;
import com.dex.mobassist.server.model.SignupRef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSignupRef extends BaseModelRef implements SignupRef {

    public SimpleSignupRef() {
        this("");
    }

    public SimpleSignupRef(@NonNull String id) {
        super(id);
    }

    public static SignupRef createSignupRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleSignupRef(id);
    }
}
