package com.dex.mobassist.server.model;

import lombok.NonNull;

import java.util.List;

public interface Member extends MemberRef {
    String getFirstName();

    String getLastName();

    String getPhone();

    String getEmail();

    String getPreferredContact();

    void setFirstName(String firstName);

    default <T extends Member> T withFirstName(String firstName) {
        setFirstName(firstName);

        return (T) this;
    }

    void setLastName(String lastName);

    default <T extends Member> T withLastName(String lastName) {
        setFirstName(lastName);

        return (T) this;
    }

    void setPhone(String phone);

    default <T extends Member> T withPhone(@NonNull String phone) {
        setPhone(phone);

        return (T) this;
    }

    void setEmail(String email);

    default <T extends Member> T withEmail(String email) {
        setEmail(email);

        return (T) this;
    }

    void setPreferredContact(String preferredContact);

    default <T extends Member> T withPreferredContact(String preferredContact) {
        setPreferredContact(preferredContact);

        return (T) this;
    }

    List<? extends MemberRoleRef> getRoles();

    void setRoles(List<? extends MemberRoleRef> roles);

    default <T extends Member> T withRoles(List<? extends MemberRoleRef> roles) {
        setRoles(roles);

        return (T) this;
    }

}
