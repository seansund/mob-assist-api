package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.SignupOptionSetRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignupOptionSetRefCargo extends BaseCargoRef implements SignupOptionSetRef {
    public SignupOptionSetRefCargo() {
        this(null);
    }

    public SignupOptionSetRefCargo(String id) {
        super(id);
    }
}
