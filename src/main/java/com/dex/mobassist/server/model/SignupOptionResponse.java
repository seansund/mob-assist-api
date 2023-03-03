package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class SignupOptionResponse {
    private SignupOptionRef option;
    private int count;
    private int assignments;

    public static SignupOptionResponse createSignupOptionResponse(SignupOptionRef option) {
        return createSignupOptionResponse(option, 0, 0);
    }

    public static SignupOptionResponse createSignupOptionResponse(SignupOptionRef option, int count, int assignments) {
        final SignupOptionResponse response = new SignupOptionResponse();

        response.option = option;
        response.count = count;
        response.assignments = assignments;

        return response;
    }

    public SignupOptionResponse addCount() {
        return addCount(1);
    }

    public SignupOptionResponse addCount(int amount) {
        count = count + amount;

        return this;
    }

    public SignupOptionResponse addAssignment() {
        return addAssignment(1);
    }

    public SignupOptionResponse addAssignment(int amount) {
        assignments = assignments + amount;

        return this;
    }
}
