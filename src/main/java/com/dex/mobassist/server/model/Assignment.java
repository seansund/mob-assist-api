package com.dex.mobassist.server.model;

public interface Assignment extends AssignmentRef {
    String getGroup();

    String getName();

    int getRow();

    void setGroup(String group);

    void setName(String name);

    void setRow(int row);
}
