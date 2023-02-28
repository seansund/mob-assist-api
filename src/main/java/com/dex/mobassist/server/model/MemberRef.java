package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class MemberRef implements ModelRef {
    @NonNull
    private String id;

    public MemberRef() {
        this("");
    }

    public MemberRef(@NonNull String id) {
        this.id = id;
    }

    public static MemberRef createMemberRef(@NonNull String id) {
        return new MemberRef(id);
    }
}
