package com.dex.mobassist.server.model;

public interface SignupOption extends SignupOptionRef {
    String getValue();

    boolean isDeclineOption();

    void setValue(String value);

    default <T extends SignupOption> T withValue(String value) {
        setValue(value);

        return (T) this;
    }

    void setDeclineOption(boolean declineOption);

    default <T extends SignupOption> T withDeclineOption(boolean declineOption) {
        setDeclineOption(declineOption);

        return (T) this;
    }
}
