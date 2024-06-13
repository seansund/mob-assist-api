package com.dex.mobassist.server.model;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface Member extends MemberRef {
    String getFirstName();

    String getLastName();

    String getPhone();

    String getEmail();

    String getPreferredContact();

    String getPassword();

    void setFirstName(String firstName);

    default <T extends Member> T withFirstName(String firstName) {
        if (Objects.nonNull(firstName)) {
            setFirstName(firstName);
        }

        return (T) this;
    }

    void setLastName(String lastName);

    default <T extends Member> T withLastName(String lastName) {
        if (Objects.nonNull(lastName)) {
            setLastName(lastName);
        }

        return (T) this;
    }

    void setPhone(String phone);

    default <T extends Member> T withPhone(@NonNull String phone) {
        if (Objects.nonNull(phone)) {
            setPhone(phone);
        }

        return (T) this;
    }

    void setEmail(String email);

    default <T extends Member> T withEmail(String email) {
        if (Objects.nonNull(email)) {
            setEmail(email);
        }

        return (T) this;
    }

    void setPreferredContact(String preferredContact);

    default <T extends Member> T withPreferredContact(String preferredContact) {
        if (Objects.nonNull(preferredContact)) {
            setPreferredContact(preferredContact);
        }

        return (T) this;
    }

    List<? extends MemberRoleRef> getRoles();

    void setRoles(List<? extends MemberRoleRef> roles);

    default <T extends Member> T withRoles(List<? extends MemberRoleRef> roles) {
        if (Objects.nonNull(roles)) {
            setRoles(roles);
        }

        return (T) this;
    }

    void setPassword(String password);

    default <T extends Member> T withPassword(String password) {
        if (Objects.nonNull(password)) {
            setPassword(password);
        }

        return (T) this;
    }
}
