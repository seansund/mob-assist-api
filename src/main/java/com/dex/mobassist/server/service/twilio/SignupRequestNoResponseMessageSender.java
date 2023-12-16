package com.dex.mobassist.server.service.twilio;

import com.dex.mobassist.server.backend.MessageCreator;
import com.dex.mobassist.server.backend.TwilioConfig;
import com.dex.mobassist.server.backend.TwilioConfigData;
import com.dex.mobassist.server.cargo.NotificationResultCargo;
import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class SignupRequestNoResponseMessageSender extends AbstractMemberSignupResponseMessageSender<TwilioConfig> implements MemberSignupResponseMessageSender {
    private String prefix;

    public SignupRequestNoResponseMessageSender(TwilioConfigData config,
                                                MemberSignupResponseService service,
                                                SignupService signupService,
                                                SignupOptionSetService signupOptionSetService,
                                                SignupOptionService signupOptionService,
                                                AssignmentSetService assignmentSetService,
                                                AssignmentService assignmentService,
                                                MemberService memberService,
                                                MessageCreator messageCreator,
                                                String prefix) {
        super(config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService,
                memberService,
                messageCreator);
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
    protected Function<MemberSignupResponse, Message> sendMessage(Signup signup, List<? extends Member> members, List<? extends MemberSignupResponse> responses) {
        return (MemberSignupResponse response) -> {
            final String message = buildSignupRequestMessage(signup, loadSignupOptions(signup.getOptions()));

            return messageCreator.createMessage(
                    new PhoneNumber(response.getMember().getId()),
                    new PhoneNumber(config.getPhoneNumber()),
                    message
            );
        };
    }

    protected String getMessageSuffix() {
        return "Reply STOP to unsubscribe or OPTIONS for more options.";
    }

    protected String buildSignupConfirmMessage(Signup signup, List<? extends SignupOption> options, SignupOption selectedOption) {
        return format(
                "%s%s is %s. You are signed up for the %s service. %s",
                prefix,
                signup.getTitle(),
                format.format(signup.getDate()),
                selectedOption.getValue(),
                getMessageSuffix()
        );
    }

    protected String buildSignupRequestMessage(Signup signup, List<? extends SignupOption> options) {
        return format(
                "%s%s is %s. Sign up by responding with %s. %s",
                prefix,
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
