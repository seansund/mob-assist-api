package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.SignupOption;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSignupOption extends SimpleSignupOptionRef implements SignupOption {
    @NonNull
    private String value;
    private boolean declineOption = false;

    public SimpleSignupOption() {
        this("");
    }

    public SimpleSignupOption(String id) {
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
        final SimpleSignupOption signupOption = new SimpleSignupOption(id);

        signupOption.value = value;
        signupOption.declineOption = declineOption;

        return signupOption;
    }
}

