package com.dex.mobassist.server.model;

import com.dex.mobassist.server.model.SignupOptionRef;
import com.dex.mobassist.server.model.SignupOptionResponse;
import lombok.Data;

@Data
public class SimpleSignupOptionResponse implements SignupOptionResponse {
    private SignupOptionRef option;
    private Integer count;
    private Integer assignments;
}
