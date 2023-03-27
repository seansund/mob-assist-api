package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.cargo.MemberCargo;
import com.dex.mobassist.server.cargo.MemberSignupResponseCargo;
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
    private final SignupOptionService optionService;
    private final SignupOptionSetService optionSetService;
    private final MemberSignupResponseService memberSignupResponseService;
    private final MemberService memberService;
    private final AssignmentSetService assignmentSetService;
    private final AssignmentService assignmentService;

    public CurrentSignupController(
            SignupService service,
            SignupOptionService optionService,
            SignupOptionSetService optionSetService,
            MemberSignupResponseService memberSignupResponseService,
            MemberService memberService,
            AssignmentSetService assignmentSetService,
            AssignmentService assignmentService
    ) {
        this.service = service;
        this.optionService = optionService;
        this.optionSetService = optionSetService;
        this.memberSignupResponseService = memberSignupResponseService;
        this.memberService = memberService;
        this.assignmentSetService = assignmentSetService;
        this.assignmentService = assignmentService;
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

    @GetMapping("/current/responses/{phone}")
    public Object getCurrentResponseForUser(@PathVariable String phone) {
        final Signup signup = service.getCurrent();

        final Optional<? extends MemberSignupResponse> result = memberSignupResponseService.getSignupResponseForUser(signup.getId(), phone);

        return result.orElseGet(() -> new MemberSignupResponseCargo(signup.getId() + "-" + phone).withSignup(signup).withMember(new MemberCargo(phone).withPhone(phone)));
    }

    @GetMapping("/current/respond")
    public MemberSignupResponse respondToCurrent(@RequestParam @NonNull String phone, @RequestParam("option") @NonNull String optionValue) {
        final Signup signup = service.getCurrent();

        return memberSignupResponseService.signUp(signup, phone, optionValue);
    }

    protected List<? extends SignupOption> getSignupOptions(@NonNull Signup signup) {
        final SignupOptionSetRef optionSetRef = signup.getOptions();

        final List<? extends SignupOptionRef> optionRefs = (optionSetRef instanceof SignupOptionSet)
                ? ((SignupOptionSet)optionSetRef).getOptions()
                : optionSetService.getById(optionSetRef.getId()).getOptions();

        return getSignupOptions(optionRefs);
    }

    protected List<? extends SignupOption> getSignupOptions(@NonNull List<? extends SignupOptionRef> optionRefs) {
        if (optionRefs.stream().allMatch(ref -> ref instanceof SignupOption)) {
            return optionRefs.stream().map(ref -> (SignupOption) ref).toList();
        }

        return optionService.findAllById(optionRefs.stream().map(ModelRef::getId).toList());
    }

    protected List<? extends Assignment> getAssignments(@NonNull Signup signup) {
        final AssignmentSetRef assignmentSetRef = signup.getAssignments();

        final List<? extends AssignmentRef> assignmentRefs = (assignmentSetRef instanceof AssignmentSet)
                ? ((AssignmentSet)assignmentSetRef).getAssignments()
                : assignmentSetService.getById(assignmentSetRef.getId()).getAssignments();

        return getAssignments(assignmentRefs);
    }

    protected List<? extends Assignment> getAssignments(@NonNull List<? extends AssignmentRef> assignmentRefs) {
        if (assignmentRefs.stream().allMatch(ref -> ref instanceof Assignment)) {
            return assignmentRefs.stream().map(ref -> (Assignment) ref).toList();
        }

        return assignmentService.findAllById(assignmentRefs.stream().map(ModelRef::getId).toList());
    }
}
