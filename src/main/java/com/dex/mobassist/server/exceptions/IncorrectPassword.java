package com.dex.mobassist.server.exceptions;

public class IncorrectPassword extends RuntimeException {

    public IncorrectPassword(String userId) {
        super(String.format("Invalid password provided for user: %s", userId));
    }
}
