package com.dex.mobassist.server.exceptions;

public class CurrentSignupNotFound extends RuntimeException {
    public CurrentSignupNotFound() {
        super("Current Signup not found");
    }
}
