package com.dex.mobassist.server.service.twilio;

import com.dex.mobassist.server.backend.TwilioBackend;
import com.dex.mobassist.server.cargo.NotificationResultCargo;
import com.dex.mobassist.server.model.MemberSignupResponse;
import com.dex.mobassist.server.model.NotificationResult;
import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupOption;
import com.dex.mobassist.server.service.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.twilio.rest.api.v2010.account.Message.creator;
import static java.lang.String.format;

public class SignupRequestMessageSender extends AbstractMemberSignupResponseMessageSender implements MemberSignupResponseMessageSender {
    public SignupRequestMessageSender(TwilioBackend config, MemberSignupResponseService service, SignupService signupService, SignupOptionSetService signupOptionSetService, SignupOptionService signupOptionService, AssignmentSetService assignmentSetService, AssignmentService assignmentService) {
        super(config, service, signupService, signupOptionSetService, signupOptionService, assignmentSetService, assignmentService);
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
    protected Function<MemberSignupResponse, Message> sendMessage(Signup signup) {
        return (MemberSignupResponse response) -> {
            final String message = response.getSelectedOption() != null
                    ? buildSignupConfirmMessage(signup, loadSignupOptions(signup.getOptions()), loadSignupOption(response.getSelectedOption()))
                    : buildSignupRequestMessage(signup, loadSignupOptions(signup.getOptions()));

            return creator(
                    new PhoneNumber(response.getMember().getId()),
                    new PhoneNumber(config.getPhoneNumber()),
                    message
            ).create();
        };
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
