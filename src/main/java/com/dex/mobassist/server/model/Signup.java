package com.dex.mobassist.server.model;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;

public interface Signup extends SignupRef {
    Date getDate();

    String getTitle();

    String getDescription();

    List<? extends SignupOptionResponse> getResponses();

    AssignmentSetRef getAssignments();

    SignupOptionSetRef getOptions();

    void setDate(Date date);

    default <T extends Signup> T withDate(Date date) {
        setDate(date);

        return (T) this;
    }

    void setTitle(String title);

    default <T extends Signup> T withTitle(String title) {
        setTitle(title);

        return (T) this;
    }

    void setDescription(String description);

    default <T extends Signup> T withDescription(String description) {
        setDescription(description);

        return (T) this;
    }

    void setResponses(List<? extends SignupOptionResponse> responses);

    default <T extends Signup> T withResponses(List<? extends SignupOptionResponse> responses) {
        setResponses(responses);

        return (T) this;
    }

    void setAssignments(AssignmentSetRef assignments);

    default <T extends Signup> T withAssignments(AssignmentSetRef assignments) {
        setAssignments(assignments);

        return (T) this;
    }

    void setOptions(SignupOptionSetRef options);

    default <T extends Signup> T withOptions(SignupOptionSetRef options) {
        setOptions(options);

        return (T) this;
    }
}
