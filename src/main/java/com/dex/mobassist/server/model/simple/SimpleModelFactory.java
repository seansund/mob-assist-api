package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("ModelFactory")
@Profile("mock")
public class SimpleModelFactory implements ModelFactory {
    @Override
    public MemberRef createMemberRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleMemberRef(id);
    }

    @Override
    public Member createMember(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleMember(id);
    }

    @Override
    public AssignmentRef createAssignmentRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleAssignmentRef(id);
    }

    @Override
    public Assignment createAssignment(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleAssignment(id);
    }

    @Override
    public AssignmentSetRef createAssignmentSetRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleAssignmentSetRef(id);
    }

    @Override
    public AssignmentSet createAssignmentSet(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleAssignmentSet(id);
    }

    @Override
    public MemberRoleRef createMemberRoleRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleMemberRoleRef(id);
    }

    @Override
    public MemberRole createMemberRole(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleMemberRole(id);
    }

    @Override
    public MemberSignupResponseRef createMemberSignupResponseRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleMemberSignupResponseRef(id);
    }

    @Override
    public MemberSignupResponse createMemberSignupResponse(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleMemberSignupResponse(id);
    }

    @Override
    public SignupRef createSignupRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleSignupRef(id);
    }

    @Override
    public Signup createSignup(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleSignup(id);
    }

    @Override
    public SignupOptionRef createSignupOptionRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleSignupOptionRef(id);
    }

    @Override
    public SignupOption createSignupOption(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleSignupOption(id);
    }

    @Override
    public SignupOptionSetRef createSignupOptionSetRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleSignupOptionSetRef(id);
    }

    @Override
    public SignupOptionSet createSignupOptionSet(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleSignupOptionSet(id);
    }

    @Override
    public SignupOptionResponse createSignupOptionResponse() {
        return new SimpleSignupOptionResponse();
    }
}
