package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.repository.*;
import graphql.com.google.common.base.Strings;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.dex.mobassist.server.model.AssignmentRef.createAssignmentRefs;
import static com.dex.mobassist.server.model.MemberRef.createMemberRef;
import static com.dex.mobassist.server.model.SignupOptionRef.createSignupOptionRef;
import static com.dex.mobassist.server.model.SignupRef.createSignupRef;

@Controller
@CrossOrigin
public class MemberSignupResponseController {
    private final MemberSignupResponseRepository repository;

    private final SignupRepository signupRepository;
    private final MemberRepository memberRepository;
    private final SignupOptionRepository signupOptionRepository;
    private final AssignmentRepository assignmentRepository;

    public MemberSignupResponseController(
            MemberSignupResponseRepository repository,
            SignupRepository signupRepository,
            MemberRepository memberRepository,
            SignupOptionRepository signupOptionRepository,
            AssignmentRepository assignmentRepository
    ) {
        this.repository = repository;

        this.signupRepository = signupRepository;
        this.memberRepository = memberRepository;
        this.signupOptionRepository = signupOptionRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @QueryMapping
    public List<MemberSignupResponse> listSignupResponses() {
        return repository.list();
    }

    @QueryMapping
    public List<MemberSignupResponse> listSignupResponsesByUser(@Argument("phone") String phone) {
        return repository.listByUser(phone);
    }

    @QueryMapping
    public List<MemberSignupResponse> listSignupResponsesBySignup(@Argument("id") String id) {
        final List<MemberSignupResponse> responses = repository.listBySignup(id);

        System.out.println("Got responses for signup: " + responses.stream().map((res) -> res.getMember().getId() + "-" + res.getSelectedOption()).toList());

        return responses;
    }

    @QueryMapping
    public MemberSignupResponse getSignupResponseById(@Argument("id") String id) {
        return repository.getById(id);
    }

    @MutationMapping
    public MemberSignupResponse addUpdateSignupResponse(@Argument("id") String id, @Argument("signupId") String signupId, @Argument("memberPhone") String memberPhone, @Argument("selectedOptionId") String selectedOptionId, @Argument("assignmentIds") List<String> assignmentIds, @Argument("message") String message) {
        final MemberSignupResponse response = MemberSignupResponse.createMemberSignupResponse(
                Strings.isNullOrEmpty(id) ? "" : id,
                createSignupRef(signupId),
                createMemberRef(memberPhone),
                createSignupOptionRef(selectedOptionId),
                createAssignmentRefs(assignmentIds),
                message
        );

        return repository.addUpdate(response);
    }

    @MutationMapping
    public SimpleResult removeSignupResponse(@Argument("id") String id) {
        return new SimpleResult(repository.delete(id));
    }

    @SubscriptionMapping
    public Flux<List<MemberSignupResponse>> signupResponses() {
        return RxJava3Adapter.observableToFlux(repository.observable(), BackpressureStrategy.LATEST);
    }

    @SubscriptionMapping
    public Flux<List<MemberSignupResponse>> signupResponsesByUser(@Argument("phone") String phone) {
        return RxJava3Adapter.observableToFlux(repository.observableOfUserResponses(phone), BackpressureStrategy.LATEST);
    }

    @SubscriptionMapping
    public Flux<List<MemberSignupResponse>> signupResponsesBySignup(@Argument("id") String id) {
        return RxJava3Adapter.observableToFlux(repository.observableOfSignupResponses(id), BackpressureStrategy.LATEST);
    }

    @SchemaMapping(typeName="MemberSignupResponse", field="signup")
    protected Signup loadSignup(MemberSignupResponse response) {
        if (response.getSignup() instanceof Signup) {
            return (Signup)response.getSignup();
        }

        return signupRepository.getById(response.getSignup().getId());
    }

    @SchemaMapping(typeName="MemberSignupResponse", field="member")
    protected Member loadMember(MemberSignupResponse response) {
        System.out.println("Loading member: " + response.getMember());

        if (response.getMember() instanceof Member) {
            return (Member)response.getMember();
        }

        return memberRepository.getById(response.getMember().getId());
    }

    @SchemaMapping(typeName="MemberSignupResponse", field="selectedOption")
    protected SignupOption loadSignupOption(MemberSignupResponse response) {
        if (response.getSelectedOption() == null) {
            return null;
        }

        if (response.getSelectedOption() instanceof SignupOption) {
            return (SignupOption)response.getSelectedOption();
        }

        return signupOptionRepository.getById(response.getSelectedOption().getId());
    }

    @SchemaMapping(typeName="MemberSignupResponse", field="assignments")
    protected List<Assignment> loadAssignments(MemberSignupResponse response) {
        if (response.getAssignments() == null) {
            return null;
        }

        if (response.getAssignments().stream().allMatch((assignment) -> assignment instanceof Assignment)) {
            return response.getAssignments().stream()
                    .map((assignment) -> (Assignment)assignment)
                    .toList();
        }

        return assignmentRepository.getByIds(response.getAssignments()
                .stream()
                .map(AssignmentRef::getId)
                .toList());
    }

}
