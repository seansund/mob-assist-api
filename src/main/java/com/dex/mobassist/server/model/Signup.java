package com.dex.mobassist.server.model;

import graphql.com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Signup extends SignupRef {
    private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public Signup() {
        this("");
    }
    public Signup(@NonNull String id) {
        super(id);
    }

    private Date date;
    private String title;
    private String description;
    private List<SignupOptionResponse> responses;
    private AssignmentSetRef assignments;
    private SignupOptionSetRef options;

    public static Signup createSignup(@NonNull String dateString, @NonNull String title, @NonNull AssignmentSet assignments, @NonNull SignupOptionSet options) {
        return createSignup("", dateString, title, assignments, options);
    }

    public static Signup createSignup(int id, @NonNull String dateString, @NonNull String title, @NonNull AssignmentSet assignments, @NonNull SignupOptionSet options) {
        return createSignup(String.valueOf(id), dateString, title, assignments, options);
    }

    public static Signup createSignup(@NonNull String id, @NonNull String dateString, @NonNull String title, @NonNull AssignmentSet assignments, @NonNull SignupOptionSet options) {
        try {
            final Date date = dateFormat.parse(dateString);

            return createSignup(id, date, title, assignments, options);
        } catch (ParseException ex) {
            throw new RuntimeException("Unable to parse date: " + dateString);
        }
    }

    public static Signup createSignup(@NonNull Date date, @NonNull String title, @NonNull AssignmentSet assignments, @NonNull SignupOptionSet options) {
        return createSignup("", date, title, assignments, options);
    }

    public static Signup createSignup(int id, @NonNull Date date, @NonNull String title, @NonNull AssignmentSet assignments, @NonNull SignupOptionSet options) {
        return createSignup(String.valueOf(id), date, title, assignments, options);
    }

    public static Signup createSignup(@NonNull String id, @NonNull Date date, @NonNull String title, @NonNull AssignmentSet assignments, @NonNull SignupOptionSet options) {
        final Signup signup = new Signup(!Strings.isNullOrEmpty(id) ? id : date + "-" + title);

        signup.date = date;
        signup.title = title;
        signup.description = title + " signup";
        signup.assignments = assignments;
        signup.options = options;

        return signup;
    }

    public Signup withId(String id) {
        this.setId(id);

        return this;
    }

    public Signup withId(int id) {
        return withId(String.valueOf(id));
    }
}
