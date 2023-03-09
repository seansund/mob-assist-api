package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.repository.*;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/signup")
public class CurrentSignupController {
    private final SignupRepository repository;
    private final SignupOptionSetRepository optionSetRepository;
    private final MemberSignupResponseRepository memberSignupResponseRepository;
    private final MemberRepository memberRepository;
    private final AssignmentSetRepository assignmentSetRepository;

    public CurrentSignupController(
            SignupRepository repository,
            SignupOptionSetRepository optionSetRepository,
            MemberSignupResponseRepository memberSignupResponseRepository,
            MemberRepository memberRepository,
            AssignmentSetRepository assignmentSetRepository
    ) {
        this.repository = repository;
        this.optionSetRepository = optionSetRepository;
        this.memberSignupResponseRepository = memberSignupResponseRepository;
        this.memberRepository = memberRepository;
        this.assignmentSetRepository = assignmentSetRepository;
    }

    @GetMapping("/current")
    public Signup getCurrentSignup() {
        return repository.getCurrent();
    }

    @GetMapping("/current/options")
    public List<SignupOption> getCurrentSignupOptions() {
        return getSignupOptions(repository.getCurrent());
    }

    @GetMapping("/current/assignments")
    public List<Assignment> getCurrentSignupAssignments() {
        return getAssignments(repository.getCurrent());
    }

    @GetMapping("/current/responses")
    public List<MemberSignupResponse> getCurrentResponses() {
        final Signup signup = repository.getCurrent();

        return memberSignupResponseRepository.listBySignup(signup.getId());
    }

    @GetMapping("/current/respond")
    public MemberSignupResponse respondToCurrent(@RequestParam @NonNull String phone, @RequestParam("option") @NonNull String optionValue) {
        final Signup signup = repository.getCurrent();
        final List<SignupOption> options = getSignupOptions(signup);

        final Optional<SignupOption> selectedOption = options
                .stream()
                .filter(option -> optionValue.equals(option.getValue()))
                .findFirst();

        if (selectedOption.isEmpty()) {
            throw new RuntimeException("Unable to find signup option: " + optionValue);
        }

        final Member member = memberRepository.getById(phone);

        return memberSignupResponseRepository.signUp(signup, member, selectedOption.get());
    }

    protected List<SignupOption> getSignupOptions(@NonNull Signup signup) {
        final SignupOptionSetRef optionSetRef = signup.getOptions();
        if (optionSetRef instanceof SignupOptionSet) {
            return ((SignupOptionSet)optionSetRef).getOptions();
        }

        final SignupOptionSet optionSet = optionSetRepository.getById(optionSetRef.getId());

        return optionSet.getOptions();
    }

    protected List<Assignment> getAssignments(@NonNull Signup signup) {
        final AssignmentSetRef assignmentSetRef = signup.getAssignments();
        if (assignmentSetRef instanceof AssignmentSet) {
            return ((AssignmentSet)assignmentSetRef).getAssignments();
        }

        final AssignmentSet assignmentSet = assignmentSetRepository.getById(assignmentSetRef.getId());

        return assignmentSet.getAssignments();
    }
}
