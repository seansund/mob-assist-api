package com.dex.mobassist.server.repository.mock;

import com.dex.mobassist.server.exceptions.MemberPhoneNotFound;
import com.dex.mobassist.server.exceptions.SignupNotFound;
import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.MemberSignupResponse;
import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupOption;
import com.dex.mobassist.server.repository.MemberRepository;
import com.dex.mobassist.server.repository.MemberSignupResponseRepository;
import com.dex.mobassist.server.repository.SignupRepository;
import com.dex.mobassist.server.repository.mock.domain.SimpleMemberSignupResponse;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

@Repository("MemberSignupResponseRepository")
@Profile("db-mock")
public class MemberSignupResponseRepositoryMock extends AbstractRepositoryMock<MemberSignupResponse> implements MemberSignupResponseRepository {

    private final MemberRepository memberRepository;
    private final SignupRepository signupRepository;

    public MemberSignupResponseRepositoryMock(MemberRepository memberRepository, SignupRepository signupRepository) {
        this.memberRepository = memberRepository;
        this.signupRepository = signupRepository;
    }

    @Override
    protected <A extends MemberSignupResponse> A updateValueWithId(A value, int id) {
        System.out.println("Setting id: " + id);

        return value.withId(id);
    }

    @Override
    protected String getId(MemberSignupResponse value) {
        return value.getId();
    }

    @Override
    public List<? extends MemberSignupResponse> listByUser(String phone) {
        final List<? extends MemberSignupResponse> memberResponses = findAll()
                .stream()
                .filter((MemberSignupResponse res) -> res.getMember().getId().equals(phone))
                .toList();

        return memberResponses;
    }

    @Override
    public List<? extends MemberSignupResponse> listBySignup(String id) {

        return findAll()
                .stream()
                .filter((MemberSignupResponse res) -> res.getSignup().getId().equals(id))
                .toList();
    }

    @Override
    public Optional<? extends MemberSignupResponse> findForSignupAndMember(String id, String phone) {
        return findAll()
                .stream()
                .filter((resp) -> resp.getSignup().getId().equals(id) && resp.getMember().getId().equals(phone))
                .findFirst();
    }

    @Override
    public Observable<List<? extends MemberSignupResponse>> observableOfUserResponses(String phone) {

        onNext(listByUser(phone));

        return observable();
    }

    @Override
    public Observable<List<? extends MemberSignupResponse>> observableOfSignupResponses(String id) {

        onNext(listBySignup(id));

        return observable();
    }

    @Override
    public MemberSignupResponse signUp(@NonNull Signup signup, @NonNull Member member, @NonNull SignupOption option) {
        final List<? extends MemberSignupResponse> responses = listByUser(member.getPhone());

        final Optional<? extends MemberSignupResponse> response = responses
                .stream()
                .filter((resp) -> signup.getId().equals(resp.getSignup().getId()))
                .findFirst();

        return (MemberSignupResponse) response
                .map((resp) -> {
                    System.out.println("Setting selected option: " + option);
                    return resp.withSelectedOption(option);
                })
                .orElseGet(() -> {
                    System.out.println("Adding new response");
                    return save(new SimpleMemberSignupResponse("").withSignup(signup).withMember(member).withSelectedOption(option));
                });
    }
}
