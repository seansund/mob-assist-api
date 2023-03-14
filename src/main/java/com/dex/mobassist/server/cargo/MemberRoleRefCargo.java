package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.MemberRoleRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberRoleRefCargo extends BaseCargoRef implements MemberRoleRef {
    public MemberRoleRefCargo() {
        this(null);
    }

    public MemberRoleRefCargo(String id) {
        super(id);
    }
}
