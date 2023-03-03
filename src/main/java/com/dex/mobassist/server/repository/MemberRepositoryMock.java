package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Member;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MemberRepository")
@Profile("mock")
public class MemberRepositoryMock extends AbstractRepositoryMock<Member> implements MemberRepository {

    @Override
    protected Member updateValueWithId(Member value, int id) {
        return value;
    }

    @Override
    protected String getId(Member value) {
        return value.getPhone();
    }

    protected List<Member> preOnList(List<Member> members) {
        return members.stream().sorted().toList();
    }
}
