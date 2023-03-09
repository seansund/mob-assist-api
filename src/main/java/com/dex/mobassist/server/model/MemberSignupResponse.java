package com.dex.mobassist.server.model;

import lombok.NonNull;

import java.util.List;

public interface MemberSignupResponse extends MemberSignupResponseRef {
    SignupRef getSignup();

    MemberRef getMember();

    SignupOptionRef getSelectedOption();

    List<? extends AssignmentRef> getAssignments();

    String getMessage();

    boolean isCheckedIn();

    void setSignup(SignupRef signup);

    default <T extends MemberSignupResponse> T withSignup(SignupRef signup) {
        setSignup(signup);

        return (T) this;
    }

    void setMember(MemberRef member);

    default <T extends MemberSignupResponse> T withMember(MemberRef member) {
        setMember(member);

        return (T) this;
    }

    void setSelectedOption(SignupOptionRef selectedOption);

    default <T extends MemberSignupResponse> T withSelectedOption(SignupOptionRef selectedOption) {
        setSelectedOption(selectedOption);

        return (T) this;
    }

    void setAssignments(List<? extends AssignmentRef> assignments);

    default <T extends MemberSignupResponse> T withAssignments(List<? extends AssignmentRef> assignments) {
        setAssignments(assignments);

        return (T) this;
    }

    void setMessage(String message);

    default <T extends MemberSignupResponse> T withMessage(String message) {
        setMessage(message);

        return (T) this;
    }

    void setCheckedIn(boolean checkedIn);

    default <T extends MemberSignupResponse> T withCheckedIn(boolean checkedIn) {
        setCheckedIn(checkedIn);

        return (T) this;
    }
}
