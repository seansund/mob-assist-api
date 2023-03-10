package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.*;
import graphql.com.google.common.base.Strings;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller
@CrossOrigin
public class MemberSignupResponseController {
    private final MemberSignupResponseService service;

    private final SignupService signupService;
    private final MemberService memberService;
    private final SignupOptionService signupOptionService;
    private final AssignmentService assignmentService;

    private final ModelFactory factory;

    public MemberSignupResponseController(
            MemberSignupResponseService service,
            SignupService signupService,
            MemberService memberService,
            SignupOptionService signupOptionService,
            AssignmentService assignmentService,
            ModelFactory factory
    ) {
        this.service = service;

        this.signupService = signupService;
        this.memberService = memberService;
        this.signupOptionService = signupOptionService;
        this.assignmentService = assignmentService;

        this.factory = factory;
    }

    @QueryMapping
    public List<? extends MemberSignupResponse> listSignupResponses() {
        return service.list();
    }

    @QueryMapping
    public List<? extends MemberSignupResponse> listSignupResponsesByUser(@Argument("phone") String phone) {
        return service.listByUser(phone);
    }

    @QueryMapping
    public List<? extends MemberSignupResponse> listSignupResponsesBySignup(@Argument("id") String id) {
        final List<? extends MemberSignupResponse> responses = service.listBySignup(id);

        System.out.println("Got responses for signup: " + responses.stream().map((res) -> res.getMember().getId() + "-" + res.getSelectedOption()).toList());

        return responses;
    }

    @QueryMapping
    public MemberSignupResponse getSignupResponseById(@Argument("id") String id) {
        return service.getById(id);
    }

    @MutationMapping
    public MemberSignupResponse addUpdateSignupResponse(@Argument("id") String id, @Argument("signupId") String signupId, @Argument("memberPhone") String memberPhone, @Argument("selectedOptionId") String selectedOptionId, @Argument("assignmentIds") List<String> assignmentIds, @Argument("message") String message) {
        final MemberSignupResponse response = factory
                .createMemberSignupResponse(Strings.isNullOrEmpty(id) ? "" : id)
                .withSignup(factory.createSignupRef(signupId))
                .withMember(factory.createMemberRef(memberPhone))
                .withSelectedOption(factory.createSignupOptionRef(selectedOptionId))
                .withAssignments(factory.createAssignmentRefs(assignmentIds))
                .withMessage(message);

        return service.addUpdate(response);
    }

    @MutationMapping
    public SimpleResult removeSignupResponse(@Argument("id") String id) {
        return new SimpleResult(service.delete(id));
    }

    @MutationMapping
    public MemberSignupResponse checkIn(@Argument("id") String id) {
        return service.checkIn(id);
    }

    @MutationMapping
    public MemberSignupResponse removeCheckIn(@Argument("id") String id) {
        return service.removeCheckIn(id);
    }

    @SubscriptionMapping
    public Flux<List<? extends MemberSignupResponse>> signupResponses() {
        return RxJava3Adapter.observableToFlux(service.observable(), BackpressureStrategy.LATEST);
    }

    @SubscriptionMapping
    public Flux<List<? extends MemberSignupResponse>> signupResponsesByUser(@Argument("phone") String phone) {
        return RxJava3Adapter.observableToFlux(service.observableOfUserResponses(phone), BackpressureStrategy.LATEST);
    }

    @SubscriptionMapping
    public Flux<List<? extends MemberSignupResponse>> signupResponsesBySignup(@Argument("id") String id) {
        return RxJava3Adapter.observableToFlux(service.observableOfSignupResponses(id), BackpressureStrategy.LATEST);
    }

    @SchemaMapping(typeName="MemberSignupResponse", field="signup")
    protected Signup loadSignup(MemberSignupResponse response) {
        if (response.getSignup() instanceof Signup) {
            return (Signup)response.getSignup();
        }

        return signupService.getById(response.getSignup().getId());
    }

    @SchemaMapping(typeName="MemberSignupResponse", field="member")
    protected Member loadMember(MemberSignupResponse response) {
        System.out.println("Loading member: " + response.getMember());

        if (response.getMember() instanceof Member) {
            return (Member)response.getMember();
        }

        return memberService.getById(response.getMember().getId());
    }

    @SchemaMapping(typeName="MemberSignupResponse", field="selectedOption")
    protected SignupOption loadSignupOption(MemberSignupResponse response) {
        if (response.getSelectedOption() == null) {
            return null;
        }

        if (response.getSelectedOption() instanceof SignupOption) {
            return (SignupOption)response.getSelectedOption();
        }

        return signupOptionService.getById(response.getSelectedOption().getId());
    }

    @SchemaMapping(typeName="MemberSignupResponse", field="assignments")
    protected List<? extends Assignment> loadAssignments(MemberSignupResponse response) {
        if (response.getAssignments() == null) {
            return null;
        }

        if (response.getAssignments().stream().allMatch((assignment) -> assignment instanceof Assignment)) {
            return response.getAssignments().stream()
                    .map((assignment) -> (Assignment)assignment)
                    .toList();
        }

        return assignmentService.getByIds(response.getAssignments()
                .stream()
                .map(AssignmentRef::getId)
                .toList());
    }

}
