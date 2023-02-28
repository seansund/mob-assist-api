package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class SignupRef implements ModelRef {
    @NonNull
    private String id;

    public SignupRef() {
        this("");
    }

    public SignupRef(@NonNull String id) {
        this.id = id;
    }

    public static SignupRef createSignupRef(@NonNull String id) {
        return new SignupRef(id);
    }
}
