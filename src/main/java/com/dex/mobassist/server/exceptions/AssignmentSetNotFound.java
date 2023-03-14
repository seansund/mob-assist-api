package com.dex.mobassist.server.exceptions;

public class AssignmentSetNotFound extends EntityNotFound {
    public AssignmentSetNotFound(String id) {
        super("AssignmentSet", id);
    }
}
