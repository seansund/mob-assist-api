package com.dex.mobassist.server.model;

import java.util.Objects;

public interface MemberRole extends MemberRoleRef {
    String getName();

    void setName(String name);

    default <T extends MemberRole> T withName(String name) {
        if (Objects.nonNull(name)) {
            setName(name);
        }

        return (T) this;
    }
}
