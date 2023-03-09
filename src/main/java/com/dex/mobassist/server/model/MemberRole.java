package com.dex.mobassist.server.model;

public interface MemberRole extends MemberRoleRef {
    String getName();

    void setName(String name);

    default <T extends MemberRole> T withName(String name) {
        setName(name);

        return (T) this;
    }
}
