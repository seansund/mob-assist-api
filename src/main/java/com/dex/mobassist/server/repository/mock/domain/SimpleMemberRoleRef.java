package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.MemberRoleRef;
import com.dex.mobassist.server.repository.BaseModelRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleMemberRoleRef extends BaseModelRef implements MemberRoleRef {
    public SimpleMemberRoleRef() {
        this(null);
    }

    public SimpleMemberRoleRef(String id) {
        super(id);
    }
}
