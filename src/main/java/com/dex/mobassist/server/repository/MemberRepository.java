package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Member;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;

public interface MemberRepository {
    List<Member> list();

    Member getById(String phone);

    Member addUpdate(@NonNull Member newMember);

    boolean delete(@NonNull String phone);

    Observable<List<Member>> observable();
}
