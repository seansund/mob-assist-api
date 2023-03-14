package com.dex.mobassist.server.model;

import java.util.List;
import java.util.Objects;

public interface SignupOptionSet extends SignupOptionSetRef {
    String getName();

    List<? extends SignupOptionRef> getOptions();

    void setName(String name);

    default <T extends SignupOptionSet> T withName(String name) {
        if (Objects.nonNull(name)) {
            setName(name);
        }

        return (T) this;
    }

    void setOptions(List<? extends SignupOptionRef> options);

    default <T extends SignupOptionSet> T withOptions(List<? extends SignupOptionRef> options) {
        if (Objects.nonNull(options)) {
            setOptions(options);
        }

        return (T) this;
    }
}
