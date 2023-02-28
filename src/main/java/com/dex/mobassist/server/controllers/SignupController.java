package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.repository.AssignmentSetRepository;
import com.dex.mobassist.server.repository.SignupOptionSetRepository;
import com.dex.mobassist.server.repository.SignupRepository;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller
@CrossOrigin
public class SignupController {
    private final SignupRepository repository;
    private final AssignmentSetRepository assignmentSetRepository;
    private final SignupOptionSetRepository signupOptionSetRepository;

    public SignupController(SignupRepository repository, AssignmentSetRepository assignmentSetRepository, SignupOptionSetRepository signupOptionSetRepository) {
        this.repository = repository;
        this.assignmentSetRepository = assignmentSetRepository;
        this.signupOptionSetRepository = signupOptionSetRepository;
    }

    @SchemaMapping(typeName="Signup", field="assignmentSet")
    public AssignmentSet loadAssignmentSet(Signup signup) {
        System.out.println("Loading AssignmentSet: " + signup);
        return assignmentSetRepository.getById(signup.getAssignments().getId());
    }

    @SchemaMapping(typeName="Signup", field="options")
    public SignupOptionSet loadSignupOptionSet(Signup signup) {
        System.out.println("Loading SignupOptionSet: " + signup);
        return signupOptionSetRepository.getById(signup.getOptions().getId());
    }

    @SchemaMapping(typeName="Signup", field="responses")
    public List<SignupOptionResponse> loadSignupOptionResponses(Signup signup) {
        System.out.println("Loading SignupOptionResponses: " + signup);

        final SignupOptionSet optionSet = signupOptionSetRepository.getById(signup.getOptions().getId());

        return optionSet.getOptions().stream()
                .map(SignupOptionResponse::createSignupOptionResponse)
                .toList();
    }

    @QueryMapping
    public List<Signup> listSignups(@Argument("scope") String scopeString) {
        final SignupQueryScope scope = SignupQueryScope.lookup(scopeString);

        System.out.println("Getting signups for scope: " + scope);
        final List<Signup> signups = repository.list(scope);

        System.out.println("   Found signups: " + signups);

        return signups;
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
