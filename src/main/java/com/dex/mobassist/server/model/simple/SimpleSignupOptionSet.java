package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.SignupOption;
import com.dex.mobassist.server.model.SignupOptionSet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSignupOptionSet extends SimpleSignupOptionSetRef implements SignupOptionSet {
    private String name;
    private List<? extends SignupOption> options;

    public SimpleSignupOptionSet() {
        this("");
    }

    public SimpleSignupOptionSet(@NonNull String id) {
        super(id);
    }

    public void setId(@NonNull String id) {
        super.setId(id);

        options.forEach((SignupOption option) -> {
            option.setId(id + "-" + option.getValue());
        });
    }
}
