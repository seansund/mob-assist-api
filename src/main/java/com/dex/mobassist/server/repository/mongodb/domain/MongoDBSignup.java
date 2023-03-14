package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.model.*;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document("signup")
public class MongoDBSignup extends MongoDBSignupRef implements Signup {
    @NonNull
    private Date date = new Date();
    @NonNull
    private String title = "";
    @NonNull
    private String description = "";
    @DBRef
    private List<? extends SignupOptionResponse> responses;
    @DocumentReference(lazy=true)
    private MongoDBAssignmentSet assignments;
    @DocumentReference(lazy=true)
    private MongoDBSignupOptionSet options;

    public MongoDBSignup() {
        this(null);
    }

    public MongoDBSignup(String id) {
        super(id);
    }

    public static MongoDBSignup createSignup(SignupRef signupRef) {
        if (signupRef == null) {
            return null;
        }

        if (signupRef instanceof MongoDBSignup) {
            return (MongoDBSignup) signupRef;
        }

        final MongoDBSignup result = new MongoDBSignup(signupRef.getId());

        if (signupRef instanceof final Signup signup) {
            result.updateWith(signup);
        }

        return result;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<? extends SignupOptionResponse> getResponses() {
        return responses;
    }

    @Override
    public AssignmentSetRef getAssignments() {
        return assignments;
    }

    @Override
    public SignupOptionSetRef getOptions() {
        return options;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setResponses(List<? extends SignupOptionResponse> responses) {
        this.responses = responses;
    }

    @Override
    public void setAssignments(AssignmentSetRef assignments) {
        this.assignments = MongoDBAssignmentSet.createAssignmentSet(assignments);
    }

    @Override
    public void setOptions(SignupOptionSetRef options) {
        this.options = MongoDBSignupOptionSet.createSignupOptionSet(options);
    }

    public MongoDBSignup updateWith(Signup signup) {
        return this
                .withDate(signup.getDate())
                .withTitle(signup.getTitle())
                .withDescription(signup.getDescription())
                .withAssignments(MongoDBAssignmentSet.createAssignmentSet(signup.getAssignments()))
                .withOptions(MongoDBSignupOptionSet.createSignupOptionSet(signup.getOptions()))
                .withResponses(signup.getResponses());
    }
}
