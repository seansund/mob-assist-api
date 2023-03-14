package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.MemberSignupResponseRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberSignupResponseRefCargo extends BaseCargoRef implements MemberSignupResponseRef {
    public MemberSignupResponseRefCargo() {
    }

    public MemberSignupResponseRefCargo(String id) {
        super(id);
    }
}
