package com.dex.mobassist.server.model;

import com.dex.mobassist.server.model.simple.SimpleAssignmentRef;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;

public interface ModelFactory {
    MemberRef createMemberRef(String id);
    Member createMember(String id);
    default Member createMember(MemberRef memberRef) {
        if (memberRef == null) {
            return null;
        } else if (memberRef instanceof Member) {
            return (Member) memberRef;
        }

        return createMember(memberRef.getId());
    }

    AssignmentRef createAssignmentRef(String id);
    default List<? extends AssignmentRef> createAssignmentRefs(@NonNull List<String> ids) {
        return ids.stream()
                .map(this::createAssignmentRef)
                .filter(Objects::nonNull)
                .toList();
    }
    Assignment createAssignment(String id);
    default Assignment createAssignment(AssignmentRef assignmentRef) {
        if (assignmentRef == null) {
            return null;
        } else if (assignmentRef instanceof Assignment) {
            return (Assignment) assignmentRef;
        }

        return createAssignment(assignmentRef.getId());
    }

    AssignmentSetRef createAssignmentSetRef(String id);
    AssignmentSet createAssignmentSet(String id);
    default AssignmentSet createAssignmentSet(AssignmentSetRef assignmentSetRef) {
        if (assignmentSetRef == null) {
            return null;
        } else if (assignmentSetRef instanceof AssignmentSet) {
            return (AssignmentSet) assignmentSetRef;
        }

        return createAssignmentSet(assignmentSetRef.getId());
    }

    MemberRoleRef createMemberRoleRef(String id);
    MemberRole createMemberRole(String id);
    default MemberRole createMemberRole(MemberRoleRef memberRoleRef) {
        if (memberRoleRef == null) {
            return null;
        } else if (memberRoleRef instanceof MemberRole) {
            return (MemberRole) memberRoleRef;
        }

        return createMemberRole(memberRoleRef.getId());
    }

    MemberSignupResponseRef createMemberSignupResponseRef(String id);
    MemberSignupResponse createMemberSignupResponse(String id);
    default MemberSignupResponse createMemberSignupResponse(MemberSignupResponseRef memberSignupResponseRef) {
        if (memberSignupResponseRef == null) {
            return null;
        } else if (memberSignupResponseRef instanceof MemberSignupResponse) {
            return (MemberSignupResponse) memberSignupResponseRef;
        }

        return createMemberSignupResponse(memberSignupResponseRef.getId());
    }

    SignupRef createSignupRef(String id);
    Signup createSignup(String id);
    default Signup createSignup(SignupRef signupRef) {
        if (signupRef == null) {
            return null;
        } else if (signupRef instanceof Signup) {
            return (Signup) signupRef;
        }

        return createSignup(signupRef.getId());
    }

    SignupOptionRef createSignupOptionRef(String id);
    SignupOption createSignupOption(String id);
    default SignupOption createSignupOption(SignupOptionRef signupOptionRef) {
        if (signupOptionRef == null) {
            return null;
        } else if (signupOptionRef instanceof SignupOption) {
            return (SignupOption) signupOptionRef;
        }

        return createSignupOption(signupOptionRef.getId());
    }

    SignupOptionSetRef createSignupOptionSetRef(String id);
    SignupOptionSet createSignupOptionSet(String id);
    default SignupOptionSet createSignupOptionSet(SignupOptionSetRef signupOptionSetRef) {
        if (signupOptionSetRef == null) {
            return null;
        } else if (signupOptionSetRef instanceof SignupOptionSet) {
            return (SignupOptionSet) signupOptionSetRef;
        }

        return createSignupOptionSet(signupOptionSetRef.getId());
    }

    SignupOptionResponse createSignupOptionResponse();
}
