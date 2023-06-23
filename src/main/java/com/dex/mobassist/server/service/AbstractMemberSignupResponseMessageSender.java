package com.dex.mobassist.server.service;

import com.dex.mobassist.server.backend.MessageCreator;
import com.dex.mobassist.server.backend.NotificationConfig;
import com.dex.mobassist.server.cargo.AssignmentGroupCargo;
import com.dex.mobassist.server.model.*;
import lombok.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractMemberSignupResponseMessageSender<T extends NotificationConfig> implements MemberSignupResponseMessageSender {
    protected static final String dateFormatString = "MM/dd/yyyy";
    protected static final DateFormat format = new SimpleDateFormat(dateFormatString);

    protected final T config;
    private final MemberSignupResponseService service;
    private final SignupService signupService;
    private final SignupOptionSetService signupOptionSetService;
    private final SignupOptionService signupOptionService;
    private final AssignmentSetService assignmentSetService;
    private final AssignmentService assignmentService;
    private final MemberService memberService;
    protected final MessageCreator messageCreator;

    protected AbstractMemberSignupResponseMessageSender(
            T backend,
            MemberSignupResponseService service,
            SignupService signupService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService,
            AssignmentSetService assignmentSetService,
            AssignmentService assignmentService,
            MemberService memberService,
            MessageCreator messageCreator
    ) {
        this.config = backend;

        this.service = service;
        this.signupService = signupService;
        this.signupOptionSetService = signupOptionSetService;
        this.signupOptionService = signupOptionService;
        this.assignmentSetService = assignmentSetService;
        this.assignmentService = assignmentService;
        this.memberService = memberService;
        this.messageCreator = messageCreator;
    }

    public NotificationResult sendMessages(String signupId) {
        final Signup signup = signupService.getById(signupId);
        final List<? extends MemberSignupResponse> responses = service.listBySignup(signupId);

        final List<? extends Member> members = memberService.list();

        final List<?> statuses = responses.stream()
                .filter(filterByNotificationPreference(members))
                .filter(filterMessage())
                .map(sendMessage(signup, members, responses))
                .filter(Objects::nonNull)
                .toList();

        return buildResult().addChannel(config.getChannel(), statuses.size());
    }

    protected abstract NotificationResult buildResult();

    protected Predicate<MemberSignupResponse> filterByNotificationPreference(@NonNull List<? extends Member> members) {
        return (MemberSignupResponse resp) -> {
            final String preferredContact = members.stream()
                    .filter(member -> resp.getMember().getId().equals(member.getId()))
                    .findFirst()
                    .map(Member::getPreferredContact)
                    .orElse("text");

            return preferredContact.equals(config.getChannel());
        };
    }

    protected abstract Predicate<MemberSignupResponse> filterMessage();

    protected abstract Function<MemberSignupResponse, ?> sendMessage(Signup signup, List<? extends Member> members, List<? extends MemberSignupResponse> responses);

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

    public static Collection<? extends AssignmentGroup> groupAssignments(List<? extends Assignment> assignments) {
        Map<String, AssignmentGroupCargo> result = assignments.stream()
                .sorted((a, b) -> {
                    final List<Integer> comparisons = List.of(
                            a.getRow() - b.getRow(),
                            a.getGroup().compareTo(b.getGroup()),
                            a.getName().compareTo(b.getName())
                    );

                    return comparisons.stream()
                            .filter(val -> val != 0)
                            .findFirst()
                            .orElse(0);
                })
                .reduce(
                    new LinkedHashMap<>(),
                    (Map<String, AssignmentGroupCargo> previous, @NonNull Assignment current) -> {
                        AssignmentGroupCargo group = previous.get(current.getGroup());
                        if (group == null) {
                            group = new AssignmentGroupCargo(current.getGroup());
                            previous.put(current.getGroup(), group);
                        }

                        group.addAssignment(current);

                        return previous;
                    },
                    (Map<String, AssignmentGroupCargo> a, Map<String, AssignmentGroupCargo> b) -> a
                );

        return result.values();
    }
}
