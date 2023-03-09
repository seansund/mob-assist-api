package com.dex.mobassist.server.model;

public interface Member extends MemberRef {
    String getFirstName();

    String getLastName();

    String getPhone();

    String getEmail();

    String getPreferredContact();

    void setFirstName(String firstName);

    void setLastName(String lastName);

    void setPhone(String phone);

    void setEmail(String email);

    void setPreferredContact(String preferredContact);
}
