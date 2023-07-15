package com.dex.mobassist.server.service.gmail;

import com.dex.mobassist.server.backend.EmailNotificationConfig;
import com.dex.mobassist.server.backend.MessageCreator;
import com.dex.mobassist.server.cargo.NotificationResultCargo;
import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.*;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class SignupRequestMessageSender extends AbstractMemberSignupResponseEmailMessageSender implements MemberSignupResponseMessageSender {
    public SignupRequestMessageSender(EmailNotificationConfig config, MemberSignupResponseService service, SignupService signupService, SignupOptionSetService signupOptionSetService, SignupOptionService signupOptionService, AssignmentSetService assignmentSetService, AssignmentService assignmentService, MemberService memberService, MessageCreator messageCreator) {
        super(config, service, signupService, signupOptionSetService, signupOptionService, assignmentSetService, assignmentService, memberService, messageCreator);
    }

    @Override
    protected NotificationResult buildResult() {
        return new NotificationResultCargo("SignupRequest");
    }

    @Override
    protected Predicate<MemberSignupResponse> filterMessage() {
        return (MemberSignupResponse response) -> true;
    }

    @Override
    protected Function<MemberSignupResponse, ?> sendMessage(Signup signup, List<? extends Member> members, List<? extends MemberSignupResponse> responses) {
        final String subject = buildSubject(signup);

        return (MemberSignupResponse response) -> {
            final String message = response.getSelectedOption() != null
                    ? buildSignupConfirmMessage(signup, loadSignupOptions(signup.getOptions()), loadSignupOption(response.getSelectedOption()))
                    : buildSignupRequestMessage(signup, loadSignupOptions(signup.getOptions()));

            final String toEmail = getToEmail(response.getMember(), members);

            return sendEmail(toEmail, subject, message);
        };
    }

    @Override
    protected String buildSubject(Signup signup) {
        return String.format("%s %s: Sign up", signup.getTitle(), signup.getDate());
    }

    protected String buildSignupConfirmMessage(Signup signup, List<? extends SignupOption> options, SignupOption selectedOption) {
        return format(
                "%s is %s. You are signed up for the %s service. Respond with %s to change",
                signup.getTitle(),
                format.format(signup.getDate()),
                selectedOption.getValue(),
                options.stream().filter(val -> !val.getId().equals(selectedOption.getId())).map(SignupOption::getShortName).collect(Collectors.joining(", "))
        );
    }

    protected String buildSignupRequestMessage(Signup signup, List<? extends SignupOption> options) {
        return format(
                "%s is %s. Sign up by responding with %s",
                signup.getTitle(),
                format.format(signup.getDate()),
                options.stream().map(SignupOption::getShortName).collect(Collectors.joining(", "))
        );
    }
}
