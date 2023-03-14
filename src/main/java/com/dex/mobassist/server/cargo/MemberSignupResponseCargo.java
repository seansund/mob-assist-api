package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberSignupResponseCargo extends MemberSignupResponseRefCargo implements MemberSignupResponse {
    private String message;
    private Boolean checkedIn;
    private SignupRef signup;
    private MemberRef member;
    private SignupOptionRef selectedOption;
    private List<? extends AssignmentRef> assignments;

    public MemberSignupResponseCargo() {
        this(null);
    }

    public MemberSignupResponseCargo(String id) {
        super(id);
    }
}
