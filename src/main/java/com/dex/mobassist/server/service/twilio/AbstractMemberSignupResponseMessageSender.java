package com.dex.mobassist.server.service.twilio;

import com.dex.mobassist.server.backend.TwilioConfig;
import com.dex.mobassist.server.cargo.AssignmentGroupCargo;
import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.*;
import com.twilio.rest.api.v2010.account.Message;
import lombok.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractMemberSignupResponseMessageSender implements MemberSignupResponseMessageSender {
    protected static final String dateFormatString = "MM/dd/yyyy";
    protected static final DateFormat format = new SimpleDateFormat(dateFormatString);

    protected final TwilioConfig config;
    private final MemberSignupResponseService service;
    private final SignupService signupService;
    private final SignupOptionSetService signupOptionSetService;
    private final SignupOptionService signupOptionService;
    private final AssignmentSetService assignmentSetService;
    private final AssignmentService assignmentService;

    protected AbstractMemberSignupResponseMessageSender(
            TwilioConfig config,
            MemberSignupResponseService service,
            SignupService signupService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService,
            AssignmentSetService assignmentSetService,
            AssignmentService assignmentService
    ) {
        this.config = config;

        this.service = service;
        this.signupService = signupService;
        this.signupOptionSetService = signupOptionSetService;
        this.signupOptionService = signupOptionService;
        this.assignmentSetService = assignmentSetService;
        this.assignmentService = assignmentService;
    }

    public NotificationResult sendMessages(String signupId) {
        final Signup signup = signupService.getById(signupId);
        final List<? extends MemberSignupResponse> responses = service.listBySignup(signupId);

        final List<Message> statuses = responses.stream()
                .filter(filterMessage())
                .map(sendMessage(signup))
                .toList();

        return buildResult().withCount(statuses.size());
    }

    protected abstract NotificationResult buildResult();
    protected abstract Predicate<MemberSignupResponse> filterMessage();
    protected abstract Function<MemberSignupResponse, Message> sendMessage(Signup signup);

    protected List<? extends SignupOption> loadSignupOptions(SignupOptionSetRef optionSetRef) {
        final SignupOptionSet optionSet = loadSignupOptionSet(optionSetRef);

        return loadSignupOptions(optionSet.getOptions());
    }

    protected SignupOptionSet loadSignupOptionSet(SignupOptionSetRef optionSetRef) {
        return (optionSetRef instanceof SignupOptionSet)
                ? (SignupOptionSet) optionSetRef
                : signupOptionSetService.getById(optionSetRef.getId());
    }

    protected List<? extends SignupOption> loadSignupOptions(List<? extends SignupOptionRef> refs) {
        if (refs == null) {
            return null;
        }

        if (refs.stream().allMatch(ref -> ref instanceof SignupOption)) {
            return refs.stream().map(ref -> (SignupOption) ref).toList();
        }

        return signupOptionService.findAllById(refs.stream().map(ModelRef::getId).toList());
    }

    protected SignupOption loadSignupOption(SignupOptionRef ref) {
        if (ref == null) {
            return null;
        }

        if (ref instanceof SignupOption) {
            return (SignupOption) ref;
        }

        return signupOptionService.getById(ref.getId());
    }

    protected AssignmentSet loadAssignmentSet(AssignmentSetRef ref) {
        if (ref == null) {
            return null;
        }

        if (ref instanceof AssignmentSet) {
            return (AssignmentSet) ref;
        }

        return assignmentSetService.getById(ref.getId());
    }

    protected List<? extends Assignment> loadAssignments(AssignmentSetRef ref) {
        final AssignmentSet assignmentSet = loadAssignmentSet(ref);

        return loadAssignments(assignmentSet.getAssignments());
    }

    protected List<? extends Assignment> loadAssignments(List<? extends AssignmentRef> refs) {
        if (refs == null) {
            return null;
        }

        if (refs.stream().allMatch(ref -> ref instanceof Assignment)) {
            return refs.stream().map(ref -> (Assignment) ref).toList();
        }

        return assignmentService.findAllById(refs.stream().map(ModelRef::getId).toList());
    }

    protected Collection<? extends AssignmentGroup> groupAssignments(List<? extends Assignment> assignments) {
        Map<String, AssignmentGroupCargo> result = assignments.stream().reduce(
                new LinkedHashMap<>(),
                (Map<String, AssignmentGroupCargo> previous, @NonNull Assignment current) -> {
                    AssignmentGroupCargo group = previous.get(current.getGroup());
                    if (group == null) {
                        group = previous.put(current.getGroup(), new AssignmentGroupCargo(current.getGroup()));
                    }

                    group.addAssignment(current);

                    return previous;
                },
                (Map<String, AssignmentGroupCargo> a, Map<String, AssignmentGroupCargo> b) -> a
        );

        return result.values();
    }
}
