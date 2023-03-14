package com.dex.mobassist.server.repository.mock.domain;

import com.dex.mobassist.server.model.MemberRef;
import com.dex.mobassist.server.repository.BaseModelRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleMemberRef extends BaseModelRef implements MemberRef {
    public SimpleMemberRef() {
        this(null);
    }

    public SimpleMemberRef(String id) {
        super(id);
    }
}
