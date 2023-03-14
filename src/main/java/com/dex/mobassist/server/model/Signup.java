package com.dex.mobassist.server.model;

import com.dex.mobassist.server.exceptions.UnableToParseDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;

public interface Signup extends SignupRef {
    static final String dateFormatPattern = "MM/dd/yyyy";
    static final DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);

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
            throw new UnableToParseDate(dateFormatPattern, dateString);
        }
    }

    default <T extends Signup> T withDate(Date date) {
        if (Objects.nonNull(date)) {
            setDate(date);
        }

        return (T) this;
    }

    default <T extends Signup> T withDate(String date) {
        if (Objects.nonNull(date)) {
            setDate(date);
        }

        return (T) this;
    }

    void setTitle(String title);

    default <T extends Signup> T withTitle(String title) {
        if (Objects.nonNull(title)) {
            setTitle(title);
        }

        return (T) this;
    }

    void setDescription(String description);

    default <T extends Signup> T withDescription(String description) {
        if (Objects.nonNull(description)) {
            setDescription(description);
        }

        return (T) this;
    }

    void setResponses(List<? extends SignupOptionResponse> responses);

    default <T extends Signup> T withResponses(List<? extends SignupOptionResponse> responses) {
        if (Objects.nonNull(responses)) {
            setResponses(responses);
        }

        return (T) this;
    }

    void setAssignments(AssignmentSetRef assignments);

    default <T extends Signup> T withAssignments(AssignmentSetRef assignments) {
        if (Objects.nonNull(assignments)) {
            setAssignments(assignments);
        }

        return (T) this;
    }

    void setOptions(SignupOptionSetRef options);

    default <T extends Signup> T withOptions(SignupOptionSetRef options) {
        if (Objects.nonNull(options)) {
            setOptions(options);
        }

        return (T) this;
    }
}
