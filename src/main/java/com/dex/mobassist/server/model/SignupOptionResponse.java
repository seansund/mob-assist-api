package com.dex.mobassist.server.model;

public interface SignupOptionResponse {
    SignupOptionRef getOption();

    int getCount();

    int getAssignments();

    void setOption(SignupOptionRef option);

    default <T extends SignupOptionResponse> T withOption(SignupOptionRef option) {
        setOption(option);

        return (T) this;
    }

    void setCount(int count);

    default <T extends SignupOptionResponse> T withCount(int count) {
        setCount(count);

        return (T) this;
    }

    void setAssignments(int assignments);

    default <T extends SignupOptionResponse> T withAssignments(int assignments) {
        setAssignments(assignments);

        return (T) this;
    }

    default <T extends SignupOptionResponse> T addCount() {
        return addCount(1);
    }

    default <T extends SignupOptionResponse> T addCount(int amount) {
        setCount(getCount() + amount);

        return (T) this;
    }

    default <T extends SignupOptionResponse> T addAssignment() {
        return addAssignment(1);
    }

    default <T extends SignupOptionResponse> T addAssignment(int amount) {
        setAssignments(getAssignments() + amount);

        return (T) this;
    }
}
