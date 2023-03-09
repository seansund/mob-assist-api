package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.MemberRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleMemberRole extends SimpleMemberRoleRef implements MemberRole {
    private String name;

    public SimpleMemberRole() {
        this("");
    }

    public SimpleMemberRole(@NonNull String id) {
        super(id);
    }

    public static SimpleMemberRole createMemberRole(String id) {
        return createMemberRole(id, "");
    }

    public static SimpleMemberRole createMemberRole(String id, String name) {
        final SimpleMemberRole result = new SimpleMemberRole(id);

        result.name = name;

        return result;
    }
}
