package com.dex.mobassist.server.exceptions;

public class MemberPhoneNotFound extends EntityNotFound {
    public MemberPhoneNotFound(String phone) {
        super("Member", "phone", phone);
    }
}
