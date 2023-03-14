package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.AssignmentSetRef;
import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupOptionResponse;
import com.dex.mobassist.server.model.SignupOptionSetRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignupCargo extends SignupRefCargo implements Signup {
    private Date date;
    private String title;
    private String description;
    private List<? extends SignupOptionResponse> responses;
    private AssignmentSetRef assignments;
    private SignupOptionSetRef options;

    public SignupCargo() {
        this(null);
    }

    public SignupCargo(String id) {
        super(id);
    }
}
