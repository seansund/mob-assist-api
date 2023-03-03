package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class SignupOptionSet extends SignupOptionSetRef {
    private String name;
    private List<SignupOption> options;

    public SignupOptionSet() {
        this("");
    }

    public SignupOptionSet(@NonNull String id) {
        super(id);
    }

    public static SignupOptionSet createSignupOptionSet(@NonNull String name, @NonNull List<SignupOption> options) {
        return createSignupOptionSet("", name, options);
    }

    public static SignupOptionSet createSignupOptionSet(int id, @NonNull String name, @NonNull List<SignupOption> options) {
        return createSignupOptionSet(String.valueOf(id), name, options);
    }

    public static SignupOptionSet createSignupOptionSet(@NonNull String id, @NonNull String name, @NonNull List<SignupOption> options) {
        final SignupOptionSet set = new SignupOptionSet(id);

        set.name = name;
        set.options = options;

        return set;
    }

    public void setId(@NonNull String id) {
        super.setId(id);

        options.forEach((SignupOption option) -> {
            option.setId(id + "-" + option.getValue());
        });
    }

    public SignupOptionSet withId(@NonNull String id) {
        this.setId(id);

        return this;
    }

    public SignupOptionSet withId(int id) {
        return withId(String.valueOf(id));
    }
}
