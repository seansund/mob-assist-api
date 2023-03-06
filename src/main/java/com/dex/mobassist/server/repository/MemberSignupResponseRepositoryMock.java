package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.MemberSignupResponse;
import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupQueryScope;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.dex.mobassist.server.model.MemberSignupResponse.createMemberSignupResponse;
import static java.util.stream.Stream.concat;

@Repository("MemberSignupResponseRepository")
@Profile("mock")
public class MemberSignupResponseRepositoryMock extends AbstractRepositoryMock<MemberSignupResponse> implements MemberSignupResponseRepository {

    private final MemberRepository memberRepository;
    private final SignupRepository signupRepository;

    public MemberSignupResponseRepositoryMock(MemberRepository memberRepository, SignupRepository signupRepository) {
        this.memberRepository = memberRepository;
        this.signupRepository = signupRepository;
    }

    @Override
    protected MemberSignupResponse updateValueWithId(MemberSignupResponse value, int id) {
        return value.withId(id);
    }

    @Override
    protected String getId(MemberSignupResponse value) {
        return value.getId();
    }

    @Override
    public List<MemberSignupResponse> listByUser(String phone) {
        final Member member = memberRepository.getById(phone);
        final List<Signup> signups = signupRepository.list(SignupQueryScope.future);

        final List<MemberSignupResponse> memberResponses = list()
                .stream()
                .filter((MemberSignupResponse res) -> res.getMember().getId().equals(phone))
                .toList();

        final Predicate<Signup> signupNoResponse = (Signup signup) -> {
            final Stream<String> signupIds = memberResponses.stream().map((response) -> response.getSignup().getId());

            return signupIds.noneMatch((id) -> id.equals(signup.getId()));
        };

        final List<MemberSignupResponse> memberNoResponse = signups
                .stream()
                .filter(signupNoResponse)
                .map((signup) -> createMemberSignupResponse("", signup, member))
                .toList();

        return concat(memberResponses.stream(), memberNoResponse.stream()).toList();
    }

    @Override
    public List<MemberSignupResponse> listBySignup(String id) {
        final Signup signup = signupRepository.getById(id);
        final List<Member> members = memberRepository.list();

        final List<MemberSignupResponse> signupResponses = list()
                .stream()
                .filter((MemberSignupResponse res) -> res.getSignup().getId().equals(id))
                .toList();

        final Predicate<Member> memberNoResponse = (Member member) -> {
            final Stream<String> memberIds = signupResponses.stream().map((response) -> response.getMember().getId());

            return memberIds.noneMatch((phone) -> phone.equals(member.getId()));
        };

        final List<MemberSignupResponse> signupNoResponse = members
                .stream()
                .filter(memberNoResponse)
                .map((member) -> createMemberSignupResponse("", signup, member))
                .toList();

        final List<MemberSignupResponse> result = concat(signupResponses.stream(), signupNoResponse.stream()).toList();

        System.out.println("Signup responses for " + id + ": " + result.stream().map((resp) -> resp.getMember().getId() + "-" + resp.getSelectedOption()).toList());

        return result;
    }

    @Override
    public Observable<List<MemberSignupResponse>> observableOfUserResponses(String phone) {

        onNext(listByUser(phone));

        return observable();
    }

    @Override
    public Observable<List<MemberSignupResponse>> observableOfSignupResponses(String id) {

        onNext(listBySignup(id));

        return observable();
    }

    @Override
    public MemberSignupResponse checkIn(@NonNull String id) {
        final MemberSignupResponse response = getById(id);

        response.setCheckedIn(true);

        return response;
    }

    @Override
    public MemberSignupResponse removeCheckIn(@NonNull String id) {
        final MemberSignupResponse response = getById(id);

        response.setCheckedIn(false);

        return response;
    }
}
