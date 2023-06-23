package com.dex.mobassist.server.service.twilio;

import com.dex.mobassist.server.backend.MessageCreator;
import com.dex.mobassist.server.backend.TwilioConfigData;
import com.dex.mobassist.server.model.NotificationResult;
import com.dex.mobassist.server.service.*;
import org.springframework.stereotype.Service;

@Service("SMSNotification")
public class TwilioNotificationService implements NotificationService {

    private final MemberSignupResponseMessageSender signupRequestSender;
    private final MemberSignupResponseMessageSender signupRequestNoResponseSender;
    private final MemberSignupResponseMessageSender assignmentMessageSender;
    private final MemberSignupResponseMessageSender checkinRequestMessageSender;

    public TwilioNotificationService(
            TwilioConfigData config,
            MemberSignupResponseService service,
            SignupService signupService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService,
            AssignmentSetService assignmentSetService,
            AssignmentService assignmentService,
            MemberService memberService,
            MessageCreator messageCreator
    ) {
        this.signupRequestSender = new SignupRequestMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService,
                memberService,
                messageCreator
        );

        this.signupRequestNoResponseSender = new SignupRequestNoResponseMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService,
                memberService,
                messageCreator
        );

        this.assignmentMessageSender = new AssignmentMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService,
                memberService,
                messageCreator
        );

        this.checkinRequestMessageSender = new CheckinRequestMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService,
                memberService,
                messageCreator
        );
    }

    @Override
    public NotificationResult sendSignupRequest(String signupId) {
        return signupRequestSender.sendMessages(signupId);
    }

    @Override
    public NotificationResult sendSignupRequestToNoResponse(String signupId) {
        return signupRequestNoResponseSender.sendMessages(signupId);
    }

    @Override
    public NotificationResult sendAssignments(String signupId) {
        return assignmentMessageSender.sendMessages(signupId);
    }

    @Override
    public NotificationResult sendCheckinRequest(String signupId) {
        return checkinRequestMessageSender.sendMessages(signupId);
    }
}
