package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.SignupOption;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSignupOption extends SimpleSignupOptionRef implements SignupOption {
    @NonNull
    private String value = "";
    private String shortName = "";
    private Boolean declineOption = false;
    private Integer sortIndex = 0;

    public SimpleSignupOption() {
        this(null);
    }

    public SimpleSignupOption(String id) {
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

