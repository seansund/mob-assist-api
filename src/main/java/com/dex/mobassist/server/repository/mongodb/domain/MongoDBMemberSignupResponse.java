package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.model.*;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Document("memberSignupResponse")
public class MongoDBMemberSignupResponse extends MongoDBMemberSignupResponseRef implements MemberSignupResponse {
    public final static String collectionName = "memberSignupResponse";

    @NonNull
    @DocumentReference(lazy=true)
    private MongoDBSignup signup;
    @NonNull
    @DocumentReference(lazy=true)
    private MongoDBMember member;

    @DocumentReference(lazy=true)
    private MongoDBSignupOption selectedOption;
    @DBRef
    private List<? extends MongoDBAssignment> assignments = new ArrayList<>();
    private String message;
    private Boolean checkedIn = false;

    public MongoDBMemberSignupResponse() {
        this(null);
    }

    public MongoDBMemberSignupResponse(String id) {
        super(id);
    }

    public static MongoDBMemberSignupResponse createMemberSignupResponse(MemberSignupResponseRef responseRef) {
        if (responseRef == null) {
            return null;
        }

        final MongoDBMemberSignupResponse result = new MongoDBMemberSignupResponse(responseRef.getId());

        if (responseRef instanceof final MemberSignupResponse response) {
            result.updateWith(response);
        }

        return result;
    }

    public static List<? extends MongoDBMemberSignupResponse> createMemberSignupResponses(List<? extends MemberSignupResponseRef> refs) {
        if (refs == null) {
            return null;
        }

        return refs.stream()
                .map(MongoDBMemberSignupResponse::createMemberSignupResponse)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public SignupRef getSignup() {
        return signup;
    }

    @Override
    public MemberRef getMember() {
        return member;
    }

    @Override
    public SignupOptionRef getSelectedOption() {
        return selectedOption;
    }

    @Override
    public List<? extends AssignmentRef> getAssignments() {
        return assignments;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Boolean getCheckedIn() {
        return checkedIn;
    }

    @Override
    public void setSignup(SignupRef signup) {
        this.signup = MongoDBSignup.createSignup(signup);
    }

    @Override
    public void setMember(MemberRef member) {
        this.member = MongoDBMember.createMember(member);
    }

    @Override
    public void setSelectedOption(SignupOptionRef selectedOption) {
        this.selectedOption = MongoDBSignupOption.createSignupOption(selectedOption);
    }

    @Override
    public void setAssignments(List<? extends AssignmentRef> assignments) {
        this.assignments = assignments.stream().map(MongoDBAssignment::createAssignment).toList();
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public MongoDBMemberSignupResponse updateWith(MemberSignupResponse response) {
        return this
                .withCheckedIn(response.getCheckedIn())
                .withMessage(response.getMessage())
                .withSignup(MongoDBSignup.createSignup(response.getSignup()))
                .withMember(MongoDBMember.createMember(response.getMember()))
                .withSelectedOption(MongoDBSignupOption.createSignupOption(response.getSelectedOption()))
                .withAssignments(MongoDBAssignment.createAssignments(response.getAssignments()));
    }
}
