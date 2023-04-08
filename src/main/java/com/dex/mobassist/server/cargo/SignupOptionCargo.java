package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.SignupOption;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignupOptionCargo extends SignupOptionRefCargo implements SignupOption {
    private String value;
    private String shortName;
    private Boolean declineOption;
    private Integer sortIndex;

    public SignupOptionCargo() {
        this(null);
    }

    public SignupOptionCargo(String id) {
        super(id);
    }

    @Override
    public String getShortName() {
        if (!Strings.isNullOrEmpty(shortName)) {
            return shortName;
        }

        return value.replace(":", "").replace("am", "");
    }
}
