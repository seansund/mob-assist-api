package com.dex.mobassist.server.model;

import lombok.NonNull;

import java.util.List;
import java.util.Objects;

public interface MemberSignupResponse extends MemberSignupResponseRef {
    SignupRef getSignup();

    MemberRef getMember();

    SignupOptionRef getSelectedOption();

    List<? extends AssignmentRef> getAssignments();

    String getMessage();

    Boolean getCheckedIn();

    void setSignup(SignupRef signup);

    default <T extends MemberSignupResponse> T withSignup(SignupRef signup) {
        if (Objects.nonNull(signup)) {
            setSignup(signup);
        }

        return (T) this;
    }

    void setMember(MemberRef member);

    default <T extends MemberSignupResponse> T withMember(MemberRef member) {
        if (Objects.nonNull(member)) {
            setMember(member);
        }

        return (T) this;
    }

    void setSelectedOption(SignupOptionRef selectedOption);

    default <T extends MemberSignupResponse> T withSelectedOption(SignupOptionRef selectedOption) {
        if (Objects.nonNull(selectedOption)) {
            setSelectedOption(selectedOption);
        }

        return (T) this;
    }

    void setAssignments(List<? extends AssignmentRef> assignments);

    default <T extends MemberSignupResponse> T withAssignments(List<? extends AssignmentRef> assignments) {
        if (Objects.nonNull(assignments)) {
            setAssignments(assignments);
        }

        return (T) this;
    }

    void setMessage(String message);

    default <T extends MemberSignupResponse> T withMessage(String message) {
        if (Objects.nonNull(message)) {
            setMessage(message);
        }

        return (T) this;
    }

    void setCheckedIn(Boolean checkedIn);

    default <T extends MemberSignupResponse> T withCheckedIn(Boolean checkedIn) {
        if (Objects.nonNull(checkedIn)) {
            setCheckedIn(checkedIn);
        }

        return (T) this;
    }
}
