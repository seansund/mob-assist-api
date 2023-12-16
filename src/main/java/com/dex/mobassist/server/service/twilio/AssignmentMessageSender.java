package com.dex.mobassist.server.service.twilio;

import com.dex.mobassist.server.backend.MessageCreator;
import com.dex.mobassist.server.backend.TwilioConfig;
import com.dex.mobassist.server.backend.TwilioConfigData;
import com.dex.mobassist.server.cargo.NotificationResultCargo;
import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class AssignmentMessageSender extends AbstractMemberSignupResponseMessageSender<TwilioConfig> implements MemberSignupResponseMessageSender {
    private String prefix;

    public AssignmentMessageSender(
            TwilioConfigData config,
            MemberSignupResponseService service,
            SignupService signupService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService,
            AssignmentSetService assignmentSetService,
            AssignmentService assignmentService,
            MemberService memberService,
            MessageCreator messageCreator,
            String prefix
    ) {
        super(config, service, signupService, signupOptionSetService, signupOptionService, assignmentSetService, assignmentService, memberService, messageCreator);

        this.prefix = prefix;
    }

    @Override
    protected NotificationResult buildResult() {
        return new NotificationResultCargo().withType("Assignment");
    }

    @Override
    protected Predicate<MemberSignupResponse> filterMessage() {
        return (MemberSignupResponse response) -> response.getSelectedOption() != null
                && !Boolean.TRUE.equals(loadSignupOption(response.getSelectedOption()).getDeclineOption());
    }

    @Override
    protected Function<MemberSignupResponse, Message> sendMessage(Signup signup, List<? extends Member> members, List<? extends MemberSignupResponse> responses) {
        return (MemberSignupResponse response) -> {
            final String message = !response.getAssignments().isEmpty()
                    ? buildAssignmentMessage(signup, response.getMember(), loadSignupOption(response.getSelectedOption()), loadAssignments(response.getAssignments()), members, responses)
                    : buildNoAssignmentMessage(signup, loadSignupOption(response.getSelectedOption()));

            return messageCreator.createMessage(
                    new PhoneNumber(response.getMember().getId()),
                    new PhoneNumber(config.getPhoneNumber()),
                    message
            );
        };
    }

    protected String buildAssignmentMessage(Signup signup, MemberRef assignedTo, SignupOption selectedOption, List<? extends Assignment> assignmentList, List<? extends Member> members, List<? extends MemberSignupResponse> responses) {

        final String assignmentDisplay = buildAssignmentDisplay(assignmentList);

        final String assignmentDisplayWithPartner = buildAssignmentDisplayWithPartner(
                assignmentDisplay,
                lookupPartner(selectedOption, assignmentList, members, responses)
        );

        return format(
                "%s%s is %s. You are signed up for the %s service and assigned to %s. %s %s",
                prefix,
                signup.getTitle(),
                format.format(signup.getDate()),
                selectedOption.getValue(),
                assignmentDisplayWithPartner,
                buildAssignmentDiagramUrl(assignmentDisplay),
                getMessageSuffix()
        );
    }

    protected String buildAssignmentDisplayWithPartner(final String assignmentDisplay, final Member partner) {
        return partner != null
                ? format("%s with %s", assignmentDisplay, partner.getFirstName() + " " + partner.getLastName())
                : format("%s with no partner (yet)", assignmentDisplay);
    }

    public static String buildAssignmentDisplay(List<? extends Assignment> assignments) {
        Collection<? extends AssignmentGroup> groups = groupAssignments(assignments);

        final StringBuffer message = groups.stream().reduce(
                new StringBuffer(100),
                (StringBuffer previous, AssignmentGroup current) -> {
                    if (previous.length() == 0) {
                        previous.append(current.getGroup()).append(" - ");
                    } else {
                        previous.append(", ");
                    }

                    final String val = current.getAssignments()
                            .stream()
                            .map(Assignment::getName)
                            .collect(Collectors.joining(", "));

                    previous.append(val);

                    return previous;
                },
                (StringBuffer a, StringBuffer b) -> a
        );

        // TODO fix assignment print
        return message.toString();
    }

    public static String buildAssignmentDiagramUrl(String assignmentDisplay) {
        final String urlAssignment = assignmentDisplay
                .replaceAll(" ", "")
                .replaceAll("-", ",")
                .toLowerCase();

        return String.format("https://bit.ly/deacon-assn#%s", urlAssignment);
    }

    protected String buildNoAssignmentMessage(Signup signup, SignupOption selectedOption) {
        return format(
                "%s%s is %s. You are signed up for the %s service. %s",
                prefix,
                signup.getTitle(),
                format.format(signup.getDate()),
                selectedOption.getValue(),
                getMessageSuffix()
        );
    }

    protected String getMessageSuffix() {
        return "Reply STOP to unsubscribe or OPTIONS for more options.";
    }

    protected Member lookupPartner(SignupOption selectedOption, List<? extends Assignment> assignmentList, List<? extends Member> members, List<? extends MemberSignupResponse> responses) {
        final List<String> partnerAssignmentIds = assignmentList.stream().map(Assignment::getPartnerId).toList();

        List<? extends MemberSignupResponse> partners = responses.stream()
                .filter((MemberSignupResponse res) -> res.getSelectedOption() != null && res.getSelectedOption().getId().equals(selectedOption.getId()))
                .filter((MemberSignupResponse res) -> res.getAssignments()
                        .stream()
                        .map(ModelRef::getId)
                        .anyMatch(partnerAssignmentIds::contains))
                .toList();

        if (partners.isEmpty()) {
            return null;
        }

        if (partners.size() > 1) {
            System.out.println("Multiple partners found for assignment list: " + assignmentList);
        }

        return getMemberFromMemberRef(partners.get(0).getMember(), members);
    }

    protected Member getMemberFromMemberRef(MemberRef ref, List<? extends Member> members) {
        if (ref == null) {
            return null;
        }

        if (ref instanceof Member) {
            return (Member) ref;
        }

        return members.stream()
                .filter(member -> member.getId().equals(ref.getId()))
                .findFirst()
                .orElse(null);
    }
}
