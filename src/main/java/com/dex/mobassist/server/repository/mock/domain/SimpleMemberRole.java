package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.MemberRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleMemberRole extends SimpleMemberRoleRef implements MemberRole {
    private String name = "";

    public SimpleMemberRole() {
        this(null);
    }

    public SimpleMemberRole(String id) {
        super(id);
    }
}
