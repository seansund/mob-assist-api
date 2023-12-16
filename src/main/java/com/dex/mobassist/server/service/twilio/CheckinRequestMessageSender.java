package com.dex.mobassist.server.service.twilio;

import com.dex.mobassist.server.backend.MessageCreator;
import com.dex.mobassist.server.backend.TwilioConfigData;
import com.dex.mobassist.server.cargo.NotificationResultCargo;
import com.dex.mobassist.server.model.NotificationResult;
import com.dex.mobassist.server.service.*;

public class CheckinRequestMessageSender extends AssignmentMessageSender implements MemberSignupResponseMessageSender {
    private String prefix;

    public CheckinRequestMessageSender(TwilioConfigData config,
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
                messageCreator,
                prefix);
    }

    @Override
    protected NotificationResult buildResult() {
        return new NotificationResultCargo().withType("Checkin");
    }

    @Override
    protected String getMessageSuffix() {
        return "Reply YES to checkin, NO if you are unable to serve.";
    }
}
