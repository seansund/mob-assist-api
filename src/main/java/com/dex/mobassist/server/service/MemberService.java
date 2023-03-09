package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.Member;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;

public interface MemberService extends BaseService<Member> {
    @Override
    List<? extends Member> list();

    @Override
    Member getById(String id);

    @Override
    Member addUpdate(@NonNull Member newMember);

    @Override
    boolean delete(@NonNull String id);

    @Override
    Observable<List<? extends Member>> observable();
}
