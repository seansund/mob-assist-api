package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.MemberSignupResponseRef;
import com.dex.mobassist.server.repository.BaseModelRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleMemberSignupResponseRef extends BaseModelRef implements MemberSignupResponseRef {

    public SimpleMemberSignupResponseRef() {
        this(null);
    }

    public SimpleMemberSignupResponseRef(String id) {
        super(id);
    }
}
