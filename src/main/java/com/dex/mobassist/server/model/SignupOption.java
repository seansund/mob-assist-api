package com.dex.mobassist.server.model;

public interface SignupOption extends SignupOptionRef {
    String getValue();

    boolean isDeclineOption();

    void setValue(String value);

    void setDeclineOption(boolean declineOption);
}
