package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.SignupOptionRef;
import com.dex.mobassist.server.model.SignupOptionResponse;
import lombok.Data;

@Data
public class SimpleSignupOptionResponse implements SignupOptionResponse {
    private SignupOptionRef option;
    private int count;
    private int assignments;
}
