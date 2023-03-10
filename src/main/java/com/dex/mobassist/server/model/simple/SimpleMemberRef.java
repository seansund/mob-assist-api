package com.dex.mobassist.server.model.simple;

import com.dex.mobassist.server.model.BaseModelRef;
import com.dex.mobassist.server.model.MemberRef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleMemberRef extends BaseModelRef implements MemberRef {
    public SimpleMemberRef() {
        this("");
    }

    public SimpleMemberRef(@NonNull String id) {
        super(id);
    }
}
