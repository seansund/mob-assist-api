package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class SignupOptionRef implements ModelRef {
    @NonNull
    private String id;

    public SignupOptionRef() {
        this("");
    }

    public SignupOptionRef(@NonNull String id) {
        this.id = id;
    }

    public static SignupOptionRef createSignupOptionRef(@NonNull String id) {
        return new SignupOptionRef(id);
    }
}
