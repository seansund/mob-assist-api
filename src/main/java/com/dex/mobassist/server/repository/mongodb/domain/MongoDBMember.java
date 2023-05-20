package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.MemberRef;
import com.dex.mobassist.server.model.MemberRoleRef;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document("member")
public class MongoDBMember extends MongoDBMemberRef implements Member {
    @NonNull private String firstName = "";
    @NonNull private String lastName = "";
    @Indexed(unique = true)
    @NonNull private String phone = "";
    @NonNull private String email = "";
    @NonNull private String preferredContact = "text";
    @DBRef
    private List<? extends MongoDBMemberRole> roles = new ArrayList<>();
    @NonNull private String password = "";

    public MongoDBMember() {
        this(null);
    }

    public MongoDBMember(String id) {
        super(id);
    }

    public static MongoDBMember createMember(MemberRef memberRef) {
        if (memberRef == null) {
            return null;
        }

        if (memberRef instanceof MongoDBMember) {
            return (MongoDBMember) memberRef;
        }

        final MongoDBMember result = new MongoDBMember(memberRef.getId());

        if (memberRef instanceof final Member member) {
            result.updateWith(member);
        }

        return result;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPreferredContact() {
        return preferredContact;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPreferredContact(String preferredContact) {
        this.preferredContact = preferredContact;
    }

    @Override
    public List<? extends MemberRoleRef> getRoles() {
        return roles;
    }

    @Override
    public void setRoles(List<? extends MemberRoleRef> roles) {
        if (roles == null) {
            return;
        }

        this.roles = roles.stream().map(MongoDBMemberRole::createMemberRole).toList();
    }

    @Override
    public @NonNull String getPassword() {
        return password;
    }

    @Override
    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public MongoDBMember updateWith(Member member) {
        if (member == null) {
            return this;
        }

        return this
                .withEmail(member.getEmail())
                .withPhone(member.getPhone())
                .withFirstName(member.getFirstName())
                .withLastName(member.getLastName())
                .withPreferredContact(member.getPreferredContact())
                .withRoles(MongoDBMemberRole.createMemberRoles(member.getRoles()))
                .withPassword(member.getPassword());
    }
}
