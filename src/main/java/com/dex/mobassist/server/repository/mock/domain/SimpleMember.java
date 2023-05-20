package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.MemberRoleRef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Stream;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleMember extends SimpleMemberRef implements Comparable<SimpleMember>, Member {
    @NonNull private String firstName = "";
    @NonNull private String lastName = "";
    @NonNull private String phone = "";
    private String email = "";
    private String preferredContact = "text";
    private List<? extends MemberRoleRef> roles = List.of();
    private String password = "";

    public SimpleMember() {
        this(null);
    }

    public SimpleMember(String id) {
        super(id);
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
