package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.*;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/signup")
public class CurrentSignupController {
    private final SignupService service;
    private final SignupOptionSetService optionSetService;
    private final MemberSignupResponseService memberSignupResponseService;
    private final MemberService memberService;
    private final AssignmentSetService assignmentSetService;

    public CurrentSignupController(
            SignupService service,
            SignupOptionSetService optionSetService,
            MemberSignupResponseService memberSignupResponseService,
            MemberService memberService,
            AssignmentSetService assignmentSetService
    ) {
        this.service = service;
        this.optionSetService = optionSetService;
        this.memberSignupResponseService = memberSignupResponseService;
        this.memberService = memberService;
        this.assignmentSetService = assignmentSetService;
    }

    @GetMapping("/current")
    public Signup getCurrentSignup() {
        return service.getCurrent();
    }

    @GetMapping("/current/options")
    public List<? extends SignupOption> getCurrentSignupOptions() {
        return getSignupOptions(service.getCurrent());
    }

    @GetMapping("/current/assignments")
    public List<? extends Assignment> getCurrentSignupAssignments() {
        return getAssignments(service.getCurrent());
    }

    @GetMapping("/current/responses")
    public List<? extends MemberSignupResponse> getCurrentResponses() {
        final Signup signup = service.getCurrent();

        return memberSignupResponseService.listBySignup(signup.getId());
    }

    @GetMapping("/current/respond")
    public MemberSignupResponse respondToCurrent(@RequestParam @NonNull String phone, @RequestParam("option") @NonNull String optionValue) {
        final Signup signup = service.getCurrent();
        final List<? extends SignupOption> options = getSignupOptions(signup);

        final Optional<? extends SignupOption> selectedOption = options
                .stream()
                .filter(option -> optionValue.equals(option.getValue()))
                .findFirst();

        if (selectedOption.isEmpty()) {
            throw new RuntimeException("Unable to find signup option: " + optionValue);
        }

        final Member member = memberService.getById(phone);

        return memberSignupResponseService.signUp(signup, member, selectedOption.get());
    }

    protected List<? extends SignupOption> getSignupOptions(@NonNull Signup signup) {
        final SignupOptionSetRef optionSetRef = signup.getOptions();
        if (optionSetRef instanceof SignupOptionSet) {
            return ((SignupOptionSet)optionSetRef).getOptions();
        }

        final SignupOptionSet optionSet = optionSetService.getById(optionSetRef.getId());

        return optionSet.getOptions();
    }

    protected List<? extends Assignment> getAssignments(@NonNull Signup signup) {
        final AssignmentSetRef assignmentSetRef = signup.getAssignments();
        if (assignmentSetRef instanceof AssignmentSet) {
            return ((AssignmentSet)assignmentSetRef).getAssignments();
        }

        final AssignmentSet assignmentSet = assignmentSetService.getById(assignmentSetRef.getId());

        return assignmentSet.getAssignments();
    }
}
