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
}
