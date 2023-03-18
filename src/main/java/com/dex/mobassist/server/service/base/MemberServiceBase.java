package com.dex.mobassist.server.service.base;

import com.dex.mobassist.server.exceptions.MemberNotFound;
import com.dex.mobassist.server.exceptions.MemberPhoneNotFound;
import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.MemberRole;
import com.dex.mobassist.server.repository.MemberRepository;
import com.dex.mobassist.server.repository.MemberRoleRepository;
import com.dex.mobassist.server.service.MemberService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return repository.save(newMember);
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
}
