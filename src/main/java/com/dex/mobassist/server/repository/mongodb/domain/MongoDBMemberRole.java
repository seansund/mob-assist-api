package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.model.MemberRole;
import com.dex.mobassist.server.model.MemberRoleRef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@Document("memberRole")
public class MongoDBMemberRole extends MongoDBMemberRoleRef implements MemberRole {
    @Indexed(unique = true)
    @NonNull
    private String name = "";

    public MongoDBMemberRole() {
        this(null);
    }

    public MongoDBMemberRole(String id) {
        super(id);
    }

    public static MongoDBMemberRole createMemberRole(MemberRoleRef role) {
        if (role == null) {
            return null;
        }

        if (role instanceof MongoDBMemberRole) {
            return (MongoDBMemberRole) role;
        }

        final MongoDBMemberRole result = new MongoDBMemberRole(role.getId());

        if (role instanceof final MemberRole r) {
            result.updateWith(r);
        }

        return result;
    }

    public static List<? extends MongoDBMemberRole> createMemberRoles(List<? extends MemberRoleRef> refs) {
        if (refs == null) {
            return null;
        }

        return refs.stream().map(MongoDBMemberRole::createMemberRole).filter(Objects::nonNull).toList();
    }

    public MongoDBMemberRole updateWith(MemberRole memberRole) {
        if (memberRole == null) {
            return this;
        }

        return this.withName(memberRole.getName());
    }
}
