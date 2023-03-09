package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.SignupOptionRef;
import com.dex.mobassist.server.model.SignupOptionResponse;
import lombok.Data;

@Data
public class SimpleSignupOptionResponse implements SignupOptionResponse {
    private SignupOptionRef option;
    private int count;
    private int assignments;

    public static SignupOptionResponse createSignupOptionResponse(SignupOptionRef option) {
        return createSignupOptionResponse(option, 0, 0);
    }

    public static SignupOptionResponse createSignupOptionResponse(SignupOptionRef option, int count, int assignments) {
        final SimpleSignupOptionResponse response = new SimpleSignupOptionResponse();

        response.option = option;
        response.count = count;
        response.assignments = assignments;

        return response;
    }
}
