package com.dex.mobassist.server.exceptions;

public class SignupNotFound extends EntityNotFound {
    public SignupNotFound(String id) {
        super("Signup", id);
    }
}
