package com.dex.mobassist.server.service.twilio;

import com.dex.mobassist.server.backend.TwilioBackend;
import com.dex.mobassist.server.cargo.NotificationResultCargo;
import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.twilio.rest.api.v2010.account.Message.creator;
import static java.lang.String.format;

public class SignupRequestNoResponseMessageSender extends AbstractMemberSignupResponseMessageSender<TwilioBackend> implements MemberSignupResponseMessageSender {
    public SignupRequestNoResponseMessageSender(TwilioBackend config, MemberSignupResponseService service, SignupService signupService, SignupOptionSetService signupOptionSetService, SignupOptionService signupOptionService, AssignmentSetService assignmentSetService, AssignmentService assignmentService, MemberService memberService) {
        super(config, service, signupService, signupOptionSetService, signupOptionService, assignmentSetService, assignmentService, memberService);
    }

    @Override
    protected NotificationResult buildResult() {
        return new NotificationResultCargo("SignupRequestNoResponse");
    }

    @Override
    protected Predicate<MemberSignupResponse> filterMessage() {
        return (MemberSignupResponse response) -> response.getSelectedOption() == null;
    }

    @Override
    protected Function<MemberSignupResponse, Message> sendMessage(Signup signup, List<? extends Member> members) {
        return (MemberSignupResponse response) -> {
            final String message = buildSignupRequestMessage(signup, loadSignupOptions(signup.getOptions()));

            return creator(
                    new PhoneNumber(response.getMember().getId()),
                    new PhoneNumber(config.getPhoneNumber()),
                    message
            ).create();
        };
    }

    protected String getMessageSuffix() {
        return "Reply STOP to end messages or HELP for more options.";
    }

    protected String buildSignupConfirmMessage(Signup signup, List<? extends SignupOption> options, SignupOption selectedOption) {
        return format(
                "%s is %s. You are signed up for the %s service. %s",
                signup.getTitle(),
                format.format(signup.getDate()),
                selectedOption.getValue(),
                getMessageSuffix()
        );
    }

    protected String buildSignupRequestMessage(Signup signup, List<? extends SignupOption> options) {
        return format(
                "%s is %s. Sign up by responding with %s. %s",
                signup.getTitle(),
                format.format(signup.getDate()),
                options.stream().
                        sorted((a, b) -> a.getSortIndex() - b.getSortIndex())
                        .map(SignupOption::getShortName)
                        .collect(Collectors.joining(", ")),
                getMessageSuffix()
        );
    }
}
