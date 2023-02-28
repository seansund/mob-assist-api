package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class SignupOptionSetRef implements ModelRef {
    @NonNull
    private String id;

    public SignupOptionSetRef() {
        this("");
    }

    public SignupOptionSetRef(@NonNull String id) {
        this.id = id;
    }

    public static SignupOptionSetRef createSignupOptionSetRef(@NonNull String id) {
        return new SignupOptionSetRef(id);
    }
}
