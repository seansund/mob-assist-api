package com.dex.mobassist.server.repository.mock.domain;

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
public class SimpleSignup extends SimpleSignupRef implements Signup {

    private Date date = new Date();
    private String title = "";
    private String description = "";
    private List<? extends SignupOptionResponse> responses = List.of();
    private AssignmentSetRef assignments;
    private SignupOptionSetRef options;

    public SimpleSignup() {
        this(null);
    }
    public SimpleSignup(String id) {
        super(id);
    }
}
