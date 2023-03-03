package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.repository.*;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.NonNull;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.dex.mobassist.server.model.SignupOptionResponse.createSignupOptionResponse;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

@Controller
@CrossOrigin
public class SignupController {
    private final SignupRepository repository;
    private final AssignmentSetRepository assignmentSetRepository;
    private final SignupOptionSetRepository signupOptionSetRepository;
    private final MemberSignupResponseRepository memberSignupResponseRepository;

    public SignupController(
            SignupRepository repository,
            AssignmentSetRepository assignmentSetRepository,
            SignupOptionSetRepository signupOptionSetRepository,
            MemberSignupResponseRepository memberSignupResponseRepository
    ) {
        this.repository = repository;
        this.assignmentSetRepository = assignmentSetRepository;
        this.signupOptionSetRepository = signupOptionSetRepository;
        this.memberSignupResponseRepository = memberSignupResponseRepository;
    }

    @SchemaMapping(typeName="Signup", field="assignmentSet")
    public AssignmentSet loadAssignmentSet(Signup signup) {
        if (signup.getAssignments() instanceof AssignmentSet) {
            return (AssignmentSet) signup.getAssignments();
        }

        return assignmentSetRepository.getById(signup.getAssignments().getId());
    }

    @SchemaMapping(typeName="Signup", field="options")
    public SignupOptionSet loadSignupOptionSet(Signup signup) {
        if (signup.getOptions() instanceof SignupOptionSet) {
            return (SignupOptionSet) signup.getOptions();
        }

        return signupOptionSetRepository.getById(signup.getOptions().getId());
    }

    protected boolean isNullOrEmpty(List<? extends Object> list) {
        return list == null || list.isEmpty();
    }

    @SchemaMapping(typeName="Signup", field="responses")
    public List<SignupOptionResponse> loadSignupOptionResponses(final Signup signup) {
        final SignupOptionSet optionSet = signupOptionSetRepository.getById(signup.getOptions().getId());

        final List<SignupOptionResponse> initialResponses = optionSet.getOptions()
                .stream()
                .map(SignupOptionResponse::createSignupOptionResponse)
                .toList();

        final List<MemberSignupResponse> responses = memberSignupResponseRepository.listBySignup(signup.getId());

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

        final List<SignupOptionResponse> result = responses.stream()
                .reduce(
                        initialResponses,
                        (@NonNull final List<SignupOptionResponse> partialResult, @NonNull final MemberSignupResponse response) -> {

                            final Optional<SignupOptionResponse> signupOptionResponse = partialResult
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
                        (List<SignupOptionResponse> a, List<SignupOptionResponse> b) -> {
                            System.out.println("A: " + a);
                            System.out.println("B: " + b);
                            return a;
                        });

        System.out.println("Signup option responses: " + result);

        return result;
    }

    @QueryMapping
    public List<Signup> listSignups(@Argument("scope") String scopeString) {
        final SignupQueryScope scope = SignupQueryScope.lookup(scopeString);

        return repository.list(scope);
    }

    @QueryMapping
    public Signup getSignupById(@Argument("id") String id) {
        return repository.getById(id);
    }

    @MutationMapping
    public Signup addUpdateSignup(@Argument("id") String id, @Argument("date") String date, @Argument("title") String title, @Argument("assignmentSetId") String assignmentSetId, @Argument("optionSetId") String optionSetId) {
        final AssignmentSet assignmentSet = assignmentSetRepository.getById(assignmentSetId);
        final SignupOptionSet signupOptionSet = signupOptionSetRepository.getById(optionSetId);

        final Signup signup = Signup.createSignup(id, date, title, assignmentSet, signupOptionSet);

        return repository.addUpdate(signup);
    }

    @MutationMapping
    public SimpleResult removeSignup(@Argument("id") String id) {
        return new SimpleResult(repository.delete(id));
    }

    @SubscriptionMapping
    public Flux<List<Signup>> signups() {
        return RxJava3Adapter.observableToFlux(repository.observable(), BackpressureStrategy.LATEST);
    }
}
