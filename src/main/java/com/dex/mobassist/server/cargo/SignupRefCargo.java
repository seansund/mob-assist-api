package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.SignupRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignupRefCargo extends BaseCargoRef implements SignupRef {
    public SignupRefCargo() {
        this(null);
    }

    public SignupRefCargo(String id) {
        super(id);
    }
}
