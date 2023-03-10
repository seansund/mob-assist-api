package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.BaseModelRef;
import com.dex.mobassist.server.model.MemberSignupResponseRef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleMemberSignupResponseRef extends BaseModelRef implements MemberSignupResponseRef {

    public SimpleMemberSignupResponseRef() {
        this("");
    }

    public SimpleMemberSignupResponseRef(@NonNull String id) {
        super(id);
    }
}
