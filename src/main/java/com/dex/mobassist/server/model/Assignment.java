package com.dex.mobassist.server.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

public interface Assignment extends AssignmentRef {
    String getGroup();

    String getName();

    Integer getRow();

    String getPartnerId();

    void setGroup(String group);

    default <T extends Assignment> T withGroup(String group) {
        if (Objects.nonNull(group)) {
            setGroup(group);
        }

        return (T) this;
    }

    void setName(String name);

    default <T extends Assignment> T withName(String name) {
        if (Objects.nonNull(name)) {
            setName(name);
        }

        return (T) this;
    }

    void setRow(Integer row);

    default <T extends Assignment> T withRow(Integer row) {
        if (Objects.nonNull(row)) {
            setRow(row);
        }

        return (T) this;
    }

    void setPartnerId(String partnerId);

    default <T extends Assignment> T withPartnerId(String partnerId) {
        if (Objects.nonNull(partnerId)) {
            setPartnerId(partnerId);
        }

        return (T) this;
    }
}
