package com.dex.mobassist.server.model;

import lombok.NonNull;

public interface ModelRef {
    public String getId();

    public void setId(String id);

    public default <T extends ModelRef> T withId(int id) {
        return withId(String.valueOf(id));
    }

    public default <T extends ModelRef> T withId(String id) {
        setId(id);

        return (T) this;
    }
}
