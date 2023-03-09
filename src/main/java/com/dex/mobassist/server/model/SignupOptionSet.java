package com.dex.mobassist.server.model;

public interface SignupOptionSet extends SignupOptionSetRef {
    String getName();

    java.util.List<? extends SignupOption> getOptions();

    void setName(String name);

    void setOptions(java.util.List<? extends SignupOption> options);
}
