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

    void setMember(MemberRef member);

    void setSelectedOption(SignupOptionRef selectedOption);

    void setAssignments(List<? extends AssignmentRef> assignments);

    void setMessage(String message);

    void setCheckedIn(boolean checkedIn);

    public default <T extends MemberSignupResponse> T withSelectedOption(SignupOption option) {
        setSelectedOption(option);

        return (T) this;
    }
}
