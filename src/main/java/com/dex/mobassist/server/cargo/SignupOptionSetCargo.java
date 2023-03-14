package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.SignupOptionRef;
import com.dex.mobassist.server.model.SignupOptionSet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@Data
@NonNull
@EqualsAndHashCode(callSuper = true)
public class SignupOptionSetCargo extends SignupOptionSetRefCargo implements SignupOptionSet {
    private String name;
    private List<? extends SignupOptionRef> options;

    public SignupOptionSetCargo() {
        this(null);
    }

    public SignupOptionSetCargo(String id) {
        super(id);
    }

}
