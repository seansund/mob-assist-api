package com.dex.mobassist.server.exceptions;

public class MemberNotFound extends EntityNotFound {
    public MemberNotFound(String id) {
        super("Member", id);
    }
}
