package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberSignupResponse extends MemberSignupResponseRef {
    @NonNull
    private SignupRef signup;
    @NonNull
    private MemberRef member;

    private SignupOptionRef selectedOption;
    private List<? extends AssignmentRef> assignments = new ArrayList<>();
    private String message;
    private boolean checkedIn = false;

    public MemberSignupResponse() {
        this("");
    }

    public MemberSignupResponse(@NonNull String id) {
        super(id);
    }

    public static MemberSignupResponse createMemberSignupResponse(int id, @NonNull SignupRef signup, @NonNull MemberRef member) {
        return createMemberSignupResponse(String.valueOf(id), signup, member);
    }

    public static MemberSignupResponse createMemberSignupResponse(@NonNull String id, @NonNull SignupRef signup, @NonNull MemberRef member) {
        return createMemberSignupResponse(id, signup, member, null);
    }

    public static MemberSignupResponse createMemberSignupResponse(@NonNull String id, @NonNull SignupRef signup, @NonNull MemberRef member, SignupOptionRef selectedOption) {
        return createMemberSignupResponse(id, signup, member, selectedOption, new ArrayList<>());
    }

    public static MemberSignupResponse createMemberSignupResponse(@NonNull String id, @NonNull SignupRef signup, @NonNull MemberRef member, SignupOptionRef selectedOption, @NonNull List<? extends AssignmentRef> assignments) {
        return createMemberSignupResponse(id, signup, member, selectedOption, assignments, "");
    }

    public static MemberSignupResponse createMemberSignupResponse(@NonNull String id, @NonNull SignupRef signup, @NonNull MemberRef member, SignupOptionRef selectedOption, @NonNull List<? extends AssignmentRef> assignments, String message) {
        final MemberSignupResponse response = new MemberSignupResponse(id);

        response.signup = signup;
        response.member = member;
        response.selectedOption = selectedOption;
        if (assignments != null) {
            response.assignments = assignments;
        }
        response.message = message;

        return response;
    }

    public MemberSignupResponse withId(int id) {
        return withId(String.valueOf(id));
    }

    public MemberSignupResponse withId(@NonNull String id) {
        setId(id);

        return this;
    }

    public MemberSignupResponse withSelectedOption(SignupOption option) {
        setSelectedOption(option);

        return this;
    }
}
