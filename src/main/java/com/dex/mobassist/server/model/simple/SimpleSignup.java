package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.*;
import graphql.com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSignup extends SimpleSignupRef implements Signup {

    private Date date;
    private String title;
    private String description;
    private List<? extends SignupOptionResponse> responses;
    private AssignmentSetRef assignments;
    private SignupOptionSetRef options;

    public SimpleSignup() {
        this("");
    }
    public SimpleSignup(@NonNull String id) {
        super(id);
    }
}
