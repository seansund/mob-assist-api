package com.dex.mobassist.server.exceptions;

public class SignupOptionSetNotFound extends EntityNotFound {
    public SignupOptionSetNotFound(String id) {
        super("SignupOptionSet", id);
    }
}
