package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.MemberRole;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;

public interface MemberService extends BaseService<Member> {

    Member findByPhone(String phone);

    List<? extends MemberRole> listRoles();

    MemberRole addUpdateMemberRole(MemberRole role);

    boolean removeRole(@NonNull String id);
}
