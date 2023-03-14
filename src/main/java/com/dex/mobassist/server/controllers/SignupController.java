package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.cargo.*;
import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.*;
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
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

@Controller
@CrossOrigin
public class SignupController {
    private final SignupService service;
    private final AssignmentSetService assignmentSetService;
    private final AssignmentService assignmentService;
    private final SignupOptionSetService signupOptionSetService;
    private final SignupOptionService signupOptionService;
    private final MemberSignupResponseService memberSignupResponseService;

    public SignupController(
            SignupService service,
            AssignmentSetService assignmentSetService,
            AssignmentService assignmentService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService,
            MemberSignupResponseService memberSignupResponseService
    ) {
        this.service = service;
        this.assignmentSetService = assignmentSetService;
        this.assignmentService = assignmentService;
        this.signupOptionSetService = signupOptionSetService;
        this.signupOptionService = signupOptionService;
        this.memberSignupResponseService = memberSignupResponseService;
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

    protected SignupOptionResponse createSignupOptionResponse(SignupOptionRef option) {
        return createSignupOptionResponse(option, 0, 0);
    }

    protected SignupOptionResponse createSignupOptionResponse(SignupOptionRef option, int count, int assignments) {
        return new SimpleSignupOptionResponse()
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

        return service.listByScope(scope);
    }

    @QueryMapping
    public Signup getSignupById(@Argument("id") String id) {
        return service.getById(id);
    }

    protected Signup createSignup(String id, String date, String title, AssignmentSet assignmentSet, SignupOptionSet options) {
        return new SignupCargo(id)
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

    @QueryMapping
    public List<? extends SignupOptionSet> listSignupOptionSets() {
        return signupOptionSetService.list();
    }

    @MutationMapping
    public SignupOptionSet addUpdateSignupOptionSet(@Argument("id") String id, @Argument("name") String name, @Argument("optionIds") String optionIds) {
        final List<? extends SignupOptionRef> options = optionIds == null
                ? null
                : Stream.of(optionIds.split(",")).map(SignupOptionRefCargo::new).toList();

        final SignupOptionSet optionSet = new SignupOptionSetCargo(id)
                .withName(name)
                .withOptions(options);
        return signupOptionSetService.addUpdate(optionSet);
    }

    @MutationMapping
    public SimpleResult removeSignupOptionSet(@Argument("id") String id) {
        return new SimpleResult(signupOptionSetService.delete(id));
    }

    @QueryMapping
    public List<? extends SignupOption> listSignupOptions() {
        return signupOptionService.list();
    }

    @MutationMapping
    public SignupOption addUpdateSignupOption(@Argument("id") String id, @Argument("value") String value, @Argument("declineOption") Boolean declineOption, @Argument("sortIndex") Integer sortIndex) {
        final SignupOption signupOption = new SignupOptionCargo(id)
                .withValue(value)
                .withDeclineOption(declineOption)
                .withSortIndex(sortIndex);

        System.out.println("SignupOption values: " + signupOption);

        return signupOptionService.addUpdate(signupOption);
    }

    @MutationMapping
    public SimpleResult removeSignupOption(@Argument("id") String id) {
        return new SimpleResult(signupOptionService.delete(id));
    }

    @QueryMapping
    public List<? extends AssignmentSet> listAssignmentSets() {
        return assignmentSetService.list();
    }

    @MutationMapping
    public AssignmentSet addUpdateAssignmentSet(@Argument("id") String id, @Argument("name") String name, @Argument("assignmentIds") String assignmentIds) {
        final List<? extends AssignmentRef> assignments = assignmentIds == null
                ? null
                : Stream.of(assignmentIds.split(",")).map(AssignmentRefCargo::new).toList();


        final AssignmentSet assignmentSet = new AssignmentSetCargo(id)
                .withName(name)
                .withAssignments(assignments);

        return assignmentSetService.addUpdate(assignmentSet);
    }

    @MutationMapping
    public SimpleResult removeAssignmentSet(@Argument("id") String id) {
        return new SimpleResult(assignmentSetService.delete(id));
    }

    @QueryMapping
    public List<? extends Assignment> listAssignments() {
        return assignmentService.list();
    }

    @MutationMapping
    public Assignment addUpdateAssignment(@Argument("id") String id, @Argument("group") String group, @Argument("name") String name, @Argument("row") Integer row) {
        final Assignment assignment = new AssignmentCargo(id)
                .withGroup(group)
                .withName(name)
                .withRow(row);

        return assignmentService.addUpdate(assignment);
    }

    @MutationMapping
    public SimpleResult removeAssignment(@Argument("id") String id) {
        return new SimpleResult(assignmentService.delete(id));
    }
}
