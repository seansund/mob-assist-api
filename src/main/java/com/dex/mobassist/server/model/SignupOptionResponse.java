package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class SignupOptionResponse {
    private SignupOption option;
    private int count;
    private int assignments;

    public static SignupOptionResponse createSignupOptionResponse(@NonNull SignupOption option) {
        return createSignupOptionResponse(option, 0, 0);
    }

    public static SignupOptionResponse createSignupOptionResponse(@NonNull SignupOption option, int count, int assignments) {
        final SignupOptionResponse response = new SignupOptionResponse();

        response.option = option;
        response.count = count;
        response.assignments = assignments;

        return response;
    }
}
