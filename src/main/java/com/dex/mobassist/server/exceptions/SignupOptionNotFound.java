package com.dex.mobassist.server.exceptions;

public class SignupOptionNotFound extends EntityNotFound {
    public SignupOptionNotFound(String id) {
        super("SignupOption", id);
    }
}
