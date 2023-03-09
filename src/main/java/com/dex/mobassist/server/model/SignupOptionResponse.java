package com.dex.mobassist.server.model;

public interface SignupOptionResponse {
    SignupOptionRef getOption();

    int getCount();

    int getAssignments();

    void setOption(SignupOptionRef option);

    void setCount(int count);

    void setAssignments(int assignments);

    <T extends SignupOptionResponse> T addCount();

    <T extends SignupOptionResponse> T addCount(int amount);

    <T extends SignupOptionResponse> T addAssignment();

    <T extends SignupOptionResponse> T addAssignment(int amount);
}
