package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Member;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

@Repository("MemberRepository")
@Profile("mock")
public class MemberRepositoryMock implements MemberRepository {

    private final BehaviorSubject<List<Member>> subject;

    public MemberRepositoryMock() {
        subject = BehaviorSubject.createDefault(List.of());

        updateMembers(Arrays.asList(
                Member.create("5126535564", "Sean", "Sundberg", "seansund@gmail.com", "text"),
                Member.create("5128977929", "Harry", "Sundberg", "hasundberg@yahoo.com", "text")
            ));
    }

    @Override
    public List<Member> list() {
        return subject.getValue();
    }

    protected Predicate<Member> compareByPhone(@NonNull String phone) {
        return (Member member) -> phone.equals(member.getPhone());
    }

    @Override
    public Member getById(@NonNull String phone) {
        return subject.getValue()
                .stream()
                .filter(compareByPhone(phone))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Member not found: " + phone));
    }

    @Override
    public Member addUpdate(@NonNull Member newMember) {
        final List<Member> members = subject.getValue();

        final Predicate<Member> isNewMember = compareByPhone(newMember.getPhone());

        final Optional<Member> existingMember = members
                .stream()
                .filter(isNewMember)
                .findFirst();

        final List<Member> updatedMembers = existingMember.isPresent()
                ? members.stream().map((Member member) -> isNewMember.test(member) ? newMember : member).toList()
                : concat(members.stream(), of(newMember)).toList();

        System.out.println("Updating members: " + updatedMembers);
        updateMembers(updatedMembers);

        return newMember;
    }

    @Override
    public boolean delete(@NonNull String phone) {
        final List<Member> members = subject.getValue();

        final List<Member> result = members
                .stream()
                .filter(compareByPhone(phone))
                .toList();

        final boolean changed = members.size() != result.size();
        if (changed) {
            updateMembers(result);
        }

        return changed;
    }

    @Override
    public Observable<List<Member>> observable() {
        return subject;
    }

    protected void updateMembers(List<Member> members) {
        subject.onNext(members.stream().sorted().toList());
    }
}
