package com.dex.mobassist.server.service.base;

import com.dex.mobassist.server.exceptions.IncorrectPassword;
import com.dex.mobassist.server.exceptions.MemberNotFound;
import com.dex.mobassist.server.exceptions.MemberPhoneNotFound;
import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.MemberRole;
import com.dex.mobassist.server.repository.MemberRepository;
import com.dex.mobassist.server.repository.MemberRoleRepository;
import com.dex.mobassist.server.repository.mongodb.domain.MongoDBMember;
import com.dex.mobassist.server.service.MemberService;
import com.dex.mobassist.server.util.Strings;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("MemberService")
public class MemberServiceBase implements MemberService {
    private final MemberRepository repository;
    private final MemberRoleRepository memberRoleRepository;

    public MemberServiceBase(MemberRepository repository, MemberRoleRepository memberRoleRepository) {
        this.repository = repository;
        this.memberRoleRepository = memberRoleRepository;
    }

    @Override
    public List<? extends Member> list() {
        return repository.findAll();
    }

    @Override
    public Member getById(String id) {
        return repository.findById(id).orElseThrow(() -> new MemberNotFound(id));
    }

    @Override
    public Member addUpdate(@NonNull Member newMember) {
        final MongoDBMember member = repository
                .findByPhone(newMember.getPhone())
                .map(m -> (MongoDBMember)m)
                .orElseGet(() -> new MongoDBMember(newMember.getId()));

        member.updateWith(newMember);

        return repository.save(member);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.deleteById(id);
    }

    @Override
    public Observable<List<? extends Member>> observable() {
        return repository.observable();
    }

    @Override
    public Member findByPhone(String phone) {
        return repository.findByPhone(phone).orElseThrow(() -> new MemberPhoneNotFound(phone));
    }

    @Override
    public List<? extends MemberRole> listRoles() {
        return memberRoleRepository.findAll();
    }

    @Override
    public MemberRole addUpdateMemberRole(MemberRole role) {
        return memberRoleRepository.save(role);
    }

    @Override
    public boolean removeRole(@NonNull String id) {
        return memberRoleRepository.deleteById(id);
    }

    @Override
    public void setPreferredContact(String memberPhone, String preferredContact) {
        final Member member = findByPhone(memberPhone);

        member.setPreferredContact(preferredContact);

        this.addUpdate(member);
    }

    @Override
    public Member login(String userId, String password) {
        final Member member = getMemberFromUserId(userId).orElseThrow(() -> new MemberNotFound(userId));

        final String hashedPassword = Strings.hashString(password);

        if (!hashedPassword.equals(member.getPassword())) {
            throw new IncorrectPassword(userId);
        }

        return member;
    }

    protected Optional<? extends Member> getMemberFromUserId(String userId) {
        Optional<? extends Member> member = repository.findByPhone(userId);
        if (member.isPresent()) {
            return member;
        }

        member = repository.findByEmail(userId);
        return member;
    }
}
