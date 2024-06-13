package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Member;

import java.util.Optional;

public interface MemberRepository extends BaseRepository<Member> {
    Optional<? extends Member> findByPhone(String phone);

    Optional<? extends Member> findByEmail(String email);
}
