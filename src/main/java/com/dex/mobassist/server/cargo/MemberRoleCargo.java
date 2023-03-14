package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.MemberRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberRoleCargo extends MemberRoleRefCargo implements MemberRole {
    private String name;

    public MemberRoleCargo() {
        this(null);
    }

    public MemberRoleCargo(String id) {
        super(id);
    }
}
