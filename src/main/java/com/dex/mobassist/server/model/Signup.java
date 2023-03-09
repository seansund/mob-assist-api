package com.dex.mobassist.server.model;

public interface Signup extends SignupRef {
    java.util.Date getDate();

    String getTitle();

    String getDescription();

    java.util.List<? extends SignupOptionResponse> getResponses();

    AssignmentSetRef getAssignments();

    SignupOptionSetRef getOptions();

    void setDate(java.util.Date date);

    void setTitle(String title);

    void setDescription(String description);

    void setResponses(java.util.List<? extends SignupOptionResponse> responses);

    void setAssignments(AssignmentSetRef assignments);

    void setOptions(SignupOptionSetRef options);
}
