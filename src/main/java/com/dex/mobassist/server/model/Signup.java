package com.dex.mobassist.server.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

public interface Signup extends SignupRef {
    static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    Date getDate();

    String getTitle();

    String getDescription();

    List<? extends SignupOptionResponse> getResponses();

    AssignmentSetRef getAssignments();

    SignupOptionSetRef getOptions();

    void setDate(Date date);
    default void setDate(String dateString) {
        try {
            setDate(dateFormat.parse(dateString));
        } catch (Exception ex) {
            throw new RuntimeException("Unable to parse date: " + dateString);
        }
    }

    default <T extends Signup> T withDate(Date date) {
        setDate(date);

        return (T) this;
    }

    default <T extends Signup> T withDate(String date) {
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
