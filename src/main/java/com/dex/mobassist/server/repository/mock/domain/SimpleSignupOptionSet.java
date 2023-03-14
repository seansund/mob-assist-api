package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.SignupOption;
import com.dex.mobassist.server.model.SignupOptionRef;
import com.dex.mobassist.server.model.SignupOptionSet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSignupOptionSet extends SimpleSignupOptionSetRef implements SignupOptionSet {
    private String name = "";
    private List<? extends SignupOptionRef> options = List.of();

    public SimpleSignupOptionSet() {
        this(null);
    }

    public SimpleSignupOptionSet(String id) {
        super(id);
    }
}
