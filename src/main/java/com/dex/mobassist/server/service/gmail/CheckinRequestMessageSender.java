package com.dex.mobassist.server.service.gmail;

import com.dex.mobassist.server.backend.EmailNotificationConfig;
import com.dex.mobassist.server.cargo.NotificationResultCargo;
import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.twilio.rest.api.v2010.account.Message.creator;
import static java.lang.String.format;

public class CheckinRequestMessageSender extends AbstractMemberSignupResponseEmailMessageSender implements MemberSignupResponseMessageSender {
    public CheckinRequestMessageSender(EmailNotificationConfig config, MemberSignupResponseService service, SignupService signupService, SignupOptionSetService signupOptionSetService, SignupOptionService signupOptionService, AssignmentSetService assignmentSetService, AssignmentService assignmentService, MemberService memberService) {
        super(config, service, signupService, signupOptionSetService, signupOptionService, assignmentSetService, assignmentService, memberService);
    }

    @Override
    protected NotificationResult buildResult() {
        return new NotificationResultCargo().withType("Checkin");
    }

    @Override
    protected Predicate<MemberSignupResponse> filterMessage() {
        return (MemberSignupResponse response) -> !Boolean.TRUE.equals(loadSignupOption(response.getSelectedOption()).getDeclineOption());
    }

    @Override
    protected Function<MemberSignupResponse, ?> sendMessage(Signup signup, List<? extends Member> members) {
        final String subject = buildSubject(signup);

        return (MemberSignupResponse response) -> {
            final String message = format(
                    "%s. %s",
                    buildAssignmentMessage(signup, loadSignupOption(response.getSelectedOption()), loadAssignments(response.getAssignments())),
                    "Reply YES to checkin, NO if you are unable to serve");

            final String toEmail = getToEmail(response.getMember(), members);

            return sendEmail(toEmail, subject, message);
        };
    }

    @Override
    protected String buildSubject(Signup signup) {
        return String.format("%s %s: Check in", signup.getTitle(), signup.getDate());
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
                "%s is %s. You are signed up for the %s service and assigned to %s",
                signup.getTitle(),
                format.format(signup.getDate()),
                selectedOption.getValue(),
                assignmentString.apply(assignmentList)
        );
    }
}
