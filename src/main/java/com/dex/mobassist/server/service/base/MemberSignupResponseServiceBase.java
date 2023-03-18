package com.dex.mobassist.server.service.base;

import com.dex.mobassist.server.exceptions.MemberNotFound;
import com.dex.mobassist.server.exceptions.MemberSignupResponseNotFound;
import com.dex.mobassist.server.exceptions.SignupNotFound;
import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.repository.MemberRepository;
import com.dex.mobassist.server.repository.MemberSignupResponseRepository;
import com.dex.mobassist.server.repository.SignupRepository;
import com.dex.mobassist.server.repository.mock.domain.SimpleMemberSignupResponse;
import com.dex.mobassist.server.service.MemberSignupResponseService;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

@Service("MemberSignupResponseService")
public class MemberSignupResponseServiceBase implements MemberSignupResponseService {
    private final MemberSignupResponseRepository repository;
    private final SignupRepository signupRepository;
    private final MemberRepository memberRepository;

    public MemberSignupResponseServiceBase(
            MemberSignupResponseRepository repository,
            SignupRepository signupRepository,
            MemberRepository memberRepository
    ) {
        this.repository = repository;
        this.signupRepository = signupRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<? extends MemberSignupResponse> list() {
        return repository.findAll();
    }

    @Override
    public MemberSignupResponse getById(String id) {
        return repository.findById(id).orElseThrow(() -> new MemberSignupResponseNotFound(id));
    }

    @Override
    public MemberSignupResponse addUpdate(@NonNull MemberSignupResponse newMember) {
        return repository.save(newMember);
    }

    @Override
    public boolean delete(@NonNull String id) {
        return repository.deleteById(id);
    }

    @Override
    public Observable<List<? extends MemberSignupResponse>> observable() {
        return repository.observable();
    }

    @Override
    public List<? extends MemberSignupResponse> listByUser(String phone, SignupQueryScope scope) {
        final List<? extends MemberSignupResponse> memberResponses = repository.listByUser(phone);

        final Member member = memberRepository.findByPhone(phone).orElseThrow(() -> new MemberNotFound(phone));
        final List<? extends Signup> signups = signupRepository.findSignupsByScope(scope);

        final Predicate<? super Signup> signupNoResponse = (Signup signup) -> {
            final Stream<String> signupIds = memberResponses.stream().map((response) -> response.getSignup().getId());

            return signupIds.noneMatch((id) -> id.equals(signup.getId()));
        };

        final List<? extends MemberSignupResponse> memberNoResponse = signups
                .stream()
                .filter(signupNoResponse)
                .map(createMemberNoResponse(member))
                .toList();

        return concat(memberResponses.stream(), memberNoResponse.stream()).toList();
    }

    protected Function<SignupRef, MemberSignupResponse> createMemberNoResponse(MemberRef member) {
        return (SignupRef signup) -> (MemberSignupResponse) new SimpleMemberSignupResponse(signup.getId() + "-" + member.getId())
                .withSignup(signup)
                .withMember(member);
    }

    @Override
    public List<? extends MemberSignupResponse> listBySignup(String id) {
        return listBySignup(id, true);
    }

    @Override
    public List<? extends MemberSignupResponse> listBySignup(String id, boolean fill) {
        final List<? extends MemberSignupResponse> signupResponses = repository.listBySignup(id);

        if (!fill) {
            return signupResponses;
        }

        final Signup signup = signupRepository.findById(id).orElseThrow(() -> new SignupNotFound(id));
        final List<? extends Member> members = memberRepository.findAll();

        final Predicate<Member> memberNoResponse = (Member member) -> {
            final Stream<String> memberIds = signupResponses.stream().map((response) -> response.getMember().getId());

            return memberIds.noneMatch((phone) -> phone.equals(member.getId()));
        };

        final List<? extends MemberSignupResponse> signupNoResponse = members
                .stream()
                .filter(memberNoResponse)
                .map(createSignupNoResponse(signup))
                .toList();

        return concat(signupResponses.stream(), signupNoResponse.stream()).toList();
    }

    @Override
    public Optional<? extends MemberSignupResponse> getSignupResponseForUser(String signupId, String phone) {
        return repository.findForSignupAndMember(signupId, phone);
    }

    protected Function<MemberRef, MemberSignupResponse> createSignupNoResponse(SignupRef signup) {
        return (MemberRef member) -> (MemberSignupResponse) new SimpleMemberSignupResponse(signup.getId() + "-" + member.getId())
                .withSignup(signup)
                .withMember(member);
    }

    @Override
    public Observable<List<? extends MemberSignupResponse>> observableOfUserResponses(String phone) {
        return repository.observableOfUserResponses(phone);
    }

    @Override
    public Observable<List<? extends MemberSignupResponse>> observableOfSignupResponses(String id) {
        return repository.observableOfSignupResponses(id);
    }

    @Override
    public MemberSignupResponse checkIn(String id) {
        return repository.checkIn(id).orElseThrow(() -> new MemberSignupResponseNotFound(id));
    }

    @Override
    public MemberSignupResponse removeCheckIn(String id) {
        return repository.removeCheckIn(id).orElseThrow(() -> new MemberSignupResponseNotFound(id));
    }

    @Override
    public MemberSignupResponse signUp(Signup signup, Member member, SignupOption option) {
        return repository.signUp(signup, member, option);
    }
}
