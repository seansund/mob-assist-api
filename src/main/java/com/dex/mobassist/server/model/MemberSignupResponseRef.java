package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class MemberSignupResponseRef implements ModelRef {
    @NonNull
    private String id;

    public MemberSignupResponseRef() {
        this("");
    }

    public MemberSignupResponseRef(@NonNull String id) {
        this.id = id;
    }

    public static MemberSignupResponseRef createMemberSignupResponseRef(int id) {
        return createMemberSignupResponseRef(String.valueOf(id));
    }

    public static MemberSignupResponseRef createMemberSignupResponseRef(@NonNull String id) {
        return new MemberSignupResponseRef(id);
    }
}
