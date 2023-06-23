package com.dex.mobassist.server.service.gmail;

import com.dex.mobassist.server.backend.EmailNotificationConfig;
import com.dex.mobassist.server.backend.MessageCreator;
import com.dex.mobassist.server.cargo.NotificationResultCargo;
import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.*;
import com.google.api.services.gmail.model.Message;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class AssignmentMessageSender extends AbstractMemberSignupResponseEmailMessageSender implements MemberSignupResponseMessageSender {
    public AssignmentMessageSender(
            EmailNotificationConfig config,
            MemberSignupResponseService service,
            SignupService signupService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService,
            AssignmentSetService assignmentSetService,
            AssignmentService assignmentService,
            MemberService memberService,
            MessageCreator messageCreator

    ) {
        super(config, service, signupService, signupOptionSetService, signupOptionService, assignmentSetService, assignmentService, memberService, messageCreator);
    }

    @Override
    protected NotificationResult buildResult() {
        return new NotificationResultCargo().withType("Assignment");
    }

    @Override
    protected Predicate<MemberSignupResponse> filterMessage() {
        return (MemberSignupResponse response) -> !Boolean.TRUE.equals(loadSignupOption(response.getSelectedOption()).getDeclineOption());
    }

    @Override
    protected Function<MemberSignupResponse, Message> sendMessage(Signup signup, List<? extends Member> members, List<? extends MemberSignupResponse> responses) {
        final String subject = buildSubject(signup);

        return (MemberSignupResponse response) -> {
            final String message = !response.getAssignments().isEmpty()
                    ? buildAssignmentMessage(signup, loadSignupOption(response.getSelectedOption()), loadAssignments(response.getAssignments()))
                    : buildNoAssignmentMessage(signup, loadSignupOption(response.getSelectedOption()));

            final String toEmail = getToEmail(response.getMember(), members);

            return sendEmail(toEmail, subject, message);
        };
    }

    protected String buildSubject(Signup signup) {
        return String.format("%s %s: Assignment", signup.getTitle(), signup.getDate());
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

    protected String buildNoAssignmentMessage(Signup signup, SignupOption selectedOption) {
        return format(
                "%s is %s. You are signed up for the %s service",
                signup.getTitle(),
                format.format(signup.getDate()),
                selectedOption.getValue()
        );
    }
}
