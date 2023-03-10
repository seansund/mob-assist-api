package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.AssignmentSetService;
import com.dex.mobassist.server.service.MemberSignupResponseService;
import com.dex.mobassist.server.service.SignupOptionSetService;
import com.dex.mobassist.server.service.SignupService;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.NonNull;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

@Controller
@CrossOrigin
public class SignupController {
    private final SignupService service;
    private final AssignmentSetService assignmentSetService;
    private final SignupOptionSetService signupOptionSetService;
    private final MemberSignupResponseService memberSignupResponseService;
    private final ModelFactory factory;

    public SignupController(
            SignupService service,
            AssignmentSetService assignmentSetService,
            SignupOptionSetService signupOptionSetService,
            MemberSignupResponseService memberSignupResponseService,
            ModelFactory factory
    ) {
        this.service = service;
        this.assignmentSetService = assignmentSetService;
        this.signupOptionSetService = signupOptionSetService;
        this.memberSignupResponseService = memberSignupResponseService;
        this.factory = factory;
    }

    @SchemaMapping(typeName="Signup", field="assignmentSet")
    public AssignmentSet loadAssignmentSet(Signup signup) {
        if (signup.getAssignments() instanceof AssignmentSet) {
            return (AssignmentSet) signup.getAssignments();
        }

        return assignmentSetService.getById(signup.getAssignments().getId());
    }

    @SchemaMapping(typeName="Signup", field="options")
    public SignupOptionSet loadSignupOptionSet(Signup signup) {
        if (signup.getOptions() instanceof SignupOptionSet) {
            return (SignupOptionSet) signup.getOptions();
        }

        return signupOptionSetService.getById(signup.getOptions().getId());
    }

    protected boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    protected SignupOptionResponse createSignupOptionResponse(SignupOption option) {
        return factory.createSignupOptionResponse()
                .withOption(option);
    }

    protected SignupOptionResponse createSignupOptionResponse(SignupOptionRef option, int count, int assignments) {
        return factory.createSignupOptionResponse()
                .withOption(option)
                .withCount(count)
                .withAssignments(assignments);
    }

    @SchemaMapping(typeName="Signup", field="responses")
    public List<? extends SignupOptionResponse> loadSignupOptionResponses(final Signup signup) {
        final SignupOptionSet optionSet = signupOptionSetService.getById(signup.getOptions().getId());

        final List<? extends SignupOptionResponse> initialResponses = optionSet.getOptions()
                .stream()
                .map(this::createSignupOptionResponse)
                .toList();

        final List<? extends MemberSignupResponse> responses = memberSignupResponseService.listBySignup(signup.getId());

        final Function<MemberSignupResponse, Predicate<SignupOptionResponse>> matchSignupOption = (MemberSignupResponse memberSignupResponse) -> {
            final SignupOptionRef a = memberSignupResponse != null ? memberSignupResponse.getSelectedOption() : null;

            return (SignupOptionResponse signupOptionResponse) -> {
                final SignupOptionRef b = signupOptionResponse != null ? signupOptionResponse.getOption() : null;

                if (a == null && b == null) {
                    return true;
                } else if (a == null || b == null) {
                    return false;
                } else {
                    return a.getId().equals(b.getId());
                }
            };
        };

        final List<? extends SignupOptionResponse> result = responses.stream()
                .reduce(
                        initialResponses,
                        (@NonNull final List<? extends SignupOptionResponse> partialResult, @NonNull final MemberSignupResponse response) -> {

                            final Optional<? extends SignupOptionResponse> signupOptionResponse = partialResult
                                    .stream()
                                    .filter(matchSignupOption.apply(response))
                                    .findFirst();

                            final int assignments = isNullOrEmpty(response.getAssignments()) ? 0 : 1;
                            if (signupOptionResponse.isPresent()) {
                                signupOptionResponse.get()
                                        .addCount()
                                        .addAssignment(assignments);
                            } else {
                                return concat(
                                        partialResult.stream(),
                                        of(createSignupOptionResponse(response.getSelectedOption(), 1, assignments))
                                ).toList();
                            }

                            return partialResult;
                        },
                        (List<? extends SignupOptionResponse> a, List<? extends SignupOptionResponse> b) -> {
                            System.out.println("A: " + a);
                            System.out.println("B: " + b);
                            return a;
                        });

        System.out.println("Signup option responses: " + result);

        return result;
    }

    @QueryMapping
    public List<? extends Signup> listSignups(@Argument("scope") String scopeString) {
        final SignupQueryScope scope = SignupQueryScope.lookup(scopeString);

        return service.list(scope);
    }

    @QueryMapping
    public Signup getSignupById(@Argument("id") String id) {
        return service.getById(id);
    }

    protected Signup createSignup(String id, String date, String title, AssignmentSet assignmentSet, SignupOptionSet options) {
        return factory.createSignup(id)
                .withDate(date)
                .withTitle(title)
                .withAssignments(assignmentSet)
                .withOptions(options);
    }

    @MutationMapping
    public Signup addUpdateSignup(@Argument("id") String id, @Argument("date") String date, @Argument("title") String title, @Argument("assignmentSetId") String assignmentSetId, @Argument("optionSetId") String optionSetId) {
        final AssignmentSet assignmentSet = assignmentSetService.getById(assignmentSetId);
        final SignupOptionSet signupOptionSet = signupOptionSetService.getById(optionSetId);

        final Signup signup = createSignup(id, date, title, assignmentSet, signupOptionSet);

        return service.addUpdate(signup);
    }

    @MutationMapping
    public SimpleResult removeSignup(@Argument("id") String id) {
        return new SimpleResult(service.delete(id));
    }

    @SubscriptionMapping
    public Flux<List<? extends Signup>> signups() {
        return RxJava3Adapter.observableToFlux(service.observable(), BackpressureStrategy.LATEST);
    }
}
