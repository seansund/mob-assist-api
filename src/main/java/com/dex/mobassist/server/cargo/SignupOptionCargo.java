package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.SignupOption;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignupOptionCargo extends SignupOptionRefCargo implements SignupOption {
    private String value;
    private Boolean declineOption;
    private Integer sortIndex;

    public SignupOptionCargo() {
        this(null);
    }

    public SignupOptionCargo(String id) {
        super(id);
    }

}
