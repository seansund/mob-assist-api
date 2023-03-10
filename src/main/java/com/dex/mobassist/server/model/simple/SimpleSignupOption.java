package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.SignupOption;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSignupOption extends SimpleSignupOptionRef implements SignupOption {
    @NonNull
    private String value;
    private boolean declineOption = false;

    public SimpleSignupOption() {
        this("");
    }

    public SimpleSignupOption(String id) {
        super(id);
    }
}

