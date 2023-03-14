package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleMemberSignupResponse extends SimpleMemberSignupResponseRef implements MemberSignupResponse {
    @NonNull
    private SignupRef signup;
    @NonNull
    private MemberRef member;

    private SignupOptionRef selectedOption;
    private List<? extends AssignmentRef> assignments = new ArrayList<>();
    private String message;
    private Boolean checkedIn = false;

    public SimpleMemberSignupResponse() {
        this(null);
    }

    public SimpleMemberSignupResponse(String id) {
        super(id);
    }
}
