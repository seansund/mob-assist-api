package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.BaseModelRef;
import com.dex.mobassist.server.model.MemberRoleRef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleMemberRoleRef extends BaseModelRef implements MemberRoleRef {
    public SimpleMemberRoleRef() {
        this("");
    }

    public SimpleMemberRoleRef(@NonNull String id) {
        super(id);
    }

    public static SimpleMemberRoleRef createMemberRoleRef(String id) {
        if (id == null) {
            return null;
        }

        return new SimpleMemberRoleRef(id);
    }
}
