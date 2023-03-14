package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.MemberRoleRef;
import com.dex.mobassist.server.util.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberCargo extends MemberRefCargo implements Member, Comparable {
    private String phone;
    private String email;
    private String firstName;
    private String lastName;
    private String preferredContact;
    private List<? extends MemberRoleRef> roles;

    public MemberCargo() {
        this(null);
    }

    public MemberCargo(String id) {
        super(id);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof final Member that) {
            return Strings.compare(this.getPhone(), that.getPhone());
        }

        return -1;
    }
}
