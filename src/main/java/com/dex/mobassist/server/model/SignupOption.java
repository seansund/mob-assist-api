package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignupOption extends SignupOptionRef {
    @NonNull
    private String value;
    private boolean declineOption = false;

    public SignupOption() {
        this("");
    }

    public SignupOption(String id) {
        super(id);
    }

    public static SignupOption createSignupOption(@NonNull String value) {
        return createSignupOption("", value);
    }

    public static SignupOption createSignupOption(int id, @NonNull String value) {
        return createSignupOption(String.valueOf(id), value);
    }

    public static SignupOption createSignupOption(@NonNull String id, @NonNull String value) {
        return createSignupOption(id, value, false);
    }

    public static SignupOption createSignupOption(int id, String value, boolean declineOption) {
        return createSignupOption(String.valueOf(id), value, declineOption);
    }

    public static SignupOption createSignupOption(@NonNull String value, boolean declineOption) {
        return createSignupOption("", value, declineOption);
    }

    public static SignupOption createSignupOption(@NonNull String id, String value, boolean declineOption) {
        final SignupOption signupOption = new SignupOption(id);

        signupOption.value = value;
        signupOption.declineOption = declineOption;

        return signupOption;
    }

    public SignupOption withId(int id) {
        return withId(String.valueOf(id));
    }

    public SignupOption withId(String id) {
        this.setId(id);

        return this;
    }
}

