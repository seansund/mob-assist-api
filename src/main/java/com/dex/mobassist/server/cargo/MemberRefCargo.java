package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.MemberRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberRefCargo extends BaseCargoRef implements MemberRef {
    public MemberRefCargo() {
        this(null);
    }

    public MemberRefCargo(String id) {
        super(id);
    }
}
