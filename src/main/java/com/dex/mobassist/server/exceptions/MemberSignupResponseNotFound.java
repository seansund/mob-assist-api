package com.dex.mobassist.server.exceptions;

public class MemberSignupResponseNotFound extends EntityNotFound {
    public MemberSignupResponseNotFound(String id) {
        super("MemberSignupResponse", id);
    }
}
