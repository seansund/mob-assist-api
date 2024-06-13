package com.dex.mobassist.server.repository.mock;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.repository.MemberRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("MemberRepository")
@Profile("db-mock")
public class MemberRepositoryMock extends AbstractRepositoryMock<Member> implements MemberRepository {

    @Override
    protected Member updateValueWithId(Member value, int id) {
        return value.withId(id);
    }

    @Override
    protected String getId(Member value) {
        return value.getPhone();
    }

    protected List<? extends Member> preOnList(List<? extends Member> members) {
        return members.stream().sorted().toList();
    }

    @Override
    public Optional<? extends Member> findByPhone(String phone) {
        return findAll()
                .stream()
                .filter((member) -> phone.equals(member.getPhone()))
                .findFirst();
    }

    @Override
    public Optional<? extends Member> findByEmail(String email) {
        return findAll()
                .stream()
                .filter((member) -> email.equals(member.getEmail()))
                .findFirst();
    }
}
