package com.dex.mobassist.server.model;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.Objects;

public interface SignupOptionResponse {
    SignupOptionRef getOption();

    Integer getCount();

    Integer getAssignments();

    void setOption(SignupOptionRef option);

    default <T extends SignupOptionResponse> T withOption(SignupOptionRef option) {
        if (Objects.nonNull(option)) {
            setOption(option);
        }

        return (T) this;
    }

    void setCount(Integer count);

    default <T extends SignupOptionResponse> T withCount(Integer count) {
        if (Objects.nonNull(count)) {
            setCount(count);
        }

        return (T) this;
    }

    void setAssignments(Integer assignments);

    default <T extends SignupOptionResponse> T withAssignments(Integer assignments) {
        if (Objects.nonNull(assignments)) {
            setAssignments(assignments);
        }

        return (T) this;
    }

    default <T extends SignupOptionResponse> T addCount() {
        return addCount(1);
    }

    default <T extends SignupOptionResponse> T addCount(int amount) {
        setCount((Objects.isNull(getCount()) ? 0 : getCount()) + amount);

        return (T) this;
    }

    default <T extends SignupOptionResponse> T addAssignment() {
        return addAssignment(1);
    }

    default <T extends SignupOptionResponse> T addAssignment(int amount) {
        setAssignments((Objects.isNull(getAssignments()) ? 0 : getAssignments()) + amount);

        return (T) this;
    }
}
