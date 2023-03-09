package com.dex.mobassist.server.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

public interface Assignment extends AssignmentRef {
    String getGroup();

    String getName();

    int getRow();

    void setGroup(String group);

    default <T extends Assignment> T withGroup(String group) {
        setGroup(group);

        return (T) this;
    }

    void setName(String name);

    default <T extends Assignment> T withName(String name) {
        setName(name);

        return (T) this;
    }

    void setRow(int row);

    default <T extends Assignment> T withRow(int row) {
        setRow(row);

        return (T) this;
    }
}
