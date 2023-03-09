package com.dex.mobassist.server.model;

import java.util.List;

public interface SignupOptionSet extends SignupOptionSetRef {
    String getName();

    List<? extends SignupOption> getOptions();

    void setName(String name);

    default <T extends SignupOptionSet> T withName(String name) {
        setName(name);

        return (T) this;
    }

    void setOptions(List<? extends SignupOption> options);

    default <T extends SignupOptionSet> T withOptions(List<? extends SignupOption> options) {
        setOptions(options);

        return (T) this;
    }
}
