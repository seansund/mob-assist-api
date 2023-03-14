package com.dex.mobassist.server.exceptions;

public class MemberRoleNotFound extends EntityNotFound {
    public MemberRoleNotFound(String id) {
        super("MemberRole", id);
    }
}
