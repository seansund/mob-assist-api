package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.BaseModelRef;
import com.dex.mobassist.server.model.SignupOptionSetRef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSignupOptionSetRef extends BaseModelRef implements SignupOptionSetRef {

    public SimpleSignupOptionSetRef() {
        this("");
    }

    public SimpleSignupOptionSetRef(@NonNull String id) {
        super(id);
    }

    public static SignupOptionSetRef createSignupOptionSetRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleSignupOptionSetRef(id);
    }
}
