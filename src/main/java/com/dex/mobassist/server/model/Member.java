package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Member implements Comparable<Member> {
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String phone;
    private String email;
    private String preferredContact;

    public static Member create(String phone, String firstName, String lastName) {
        return create(phone, firstName, lastName, "", "");
    }

    public static Member create(String phone, String firstName, String lastName, String email, String preferredContact) {
        final Member member = new Member();

        member.phone = phone;
        member.lastName = lastName;
        member.firstName = firstName;
        member.email = email;
        member.preferredContact = preferredContact;

        return member;
    }

    public Member update(Member newMember) {
        this.phone = newMember.phone;
        this.lastName = newMember.lastName;
        this.firstName = newMember.firstName;
        this.email = newMember.email;
        this.preferredContact = newMember.preferredContact;

        return this;
    }

    @Override
    public int compareTo(Member o) {
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
