package com.dex.mobassist.server.exceptions;

public class AssignmentNotFound extends EntityNotFound {
    public AssignmentNotFound(String id) {
        super("Assignment", id);
    }
}
