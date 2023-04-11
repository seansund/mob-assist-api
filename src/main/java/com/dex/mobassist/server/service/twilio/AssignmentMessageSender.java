package com.dex.mobassist.server.service.twilio;

import com.dex.mobassist.server.backend.TwilioBackend;
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

import static com.twilio.rest.api.v2010.account.Message.creator;
import static java.lang.String.format;

public class AssignmentMessageSender extends AbstractMemberSignupResponseMessageSender<TwilioBackend> implements MemberSignupResponseMessageSender {
    public AssignmentMessageSender(
            TwilioBackend config,
            MemberSignupResponseService service,
            SignupService signupService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService,
            AssignmentSetService assignmentSetService,
            AssignmentService assignmentService,
            MemberService memberService
    ) {
        super(config, service, signupService, signupOptionSetService, signupOptionService, assignmentSetService, assignmentService, memberService);
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
    protected Function<MemberSignupResponse, Message> sendMessage(Signup signup, List<? extends Member> members) {
        return (MemberSignupResponse response) -> {
            final String message = !response.getAssignments().isEmpty()
                    ? buildAssignmentMessage(signup, loadSignupOption(response.getSelectedOption()), loadAssignments(response.getAssignments()))
                    : buildNoAssignmentMessage(signup, loadSignupOption(response.getSelectedOption()));

            return creator(
                    new PhoneNumber(response.getMember().getId()),
                    new PhoneNumber(config.getPhoneNumber()),
                    message
            ).create();
        };
    }

    protected String buildAssignmentMessage(Signup signup, SignupOption selectedOption, List<? extends Assignment> assignmentList) {
        final Function<List<? extends Assignment>, String> assignmentString = (assignments) -> {
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
        };

        return format(
                "%s is %s. You are signed up for the %s service and assigned to %s. %s",
                signup.getTitle(),
                format.format(signup.getDate()),
                selectedOption.getValue(),
                assignmentString.apply(assignmentList),
                getMessageSuffix()
        );
    }

    protected String buildNoAssignmentMessage(Signup signup, SignupOption selectedOption) {
        return format(
                "%s is %s. You are signed up for the %s service. %s",
                signup.getTitle(),
                format.format(signup.getDate()),
                selectedOption.getValue(),
                getMessageSuffix()
        );
    }

    protected String getMessageSuffix() {
        return "Reply STOP to unsubscribe or OPTIONS for more options.";
    }
}
