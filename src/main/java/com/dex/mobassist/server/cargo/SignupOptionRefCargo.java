package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.SignupOptionRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignupOptionRefCargo extends BaseCargoRef implements SignupOptionRef {
    public SignupOptionRefCargo() {
        this(null);
    }

    public SignupOptionRefCargo(String id) {
        super(id);
    }
}
