package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.Member;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.stream.Stream;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleMember extends SimpleMemberRef implements Comparable<SimpleMember>, Member {
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String phone;
    private String email;
    private String preferredContact;

    public SimpleMember() {
        this("");
    }

    public SimpleMember(String id) {
        super(id);
    }

    public static Member createMember(String phone, String firstName, String lastName) {
        return createMember(phone, firstName, lastName, "", "");
    }

    public static Member createMember(String phone, String firstName, String lastName, String email, String preferredContact) {
        final SimpleMember member = new SimpleMember(phone);

        member.phone = phone;
        member.lastName = lastName;
        member.firstName = firstName;
        member.email = email;
        member.preferredContact = preferredContact;

        return member;
    }

    public Member update(SimpleMember newMember) {
        this.phone = newMember.phone;
        this.lastName = newMember.lastName;
        this.firstName = newMember.firstName;
        this.email = newMember.email;
        this.preferredContact = newMember.preferredContact;

        return this;
    }

    @Override
    public int compareTo(SimpleMember o) {
        return Stream
                .of(
                        this.lastName.compareTo(o.lastName),
                        this.firstName.compareTo(o.firstName)
                )
                .filter((Integer value) -> value != 0)
                .findFirst()
                .orElse(0);
    }
}
