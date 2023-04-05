package com.dex.mobassist.server.service.twilio;

import com.dex.mobassist.server.backend.TwilioBackend;
import com.dex.mobassist.server.backend.TwilioConfig;
import com.dex.mobassist.server.model.NotificationResult;
import com.dex.mobassist.server.service.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service("NotificationService")
@Profile("twilio")
public class TwilioNotificationService implements NotificationService {

    private final MemberSignupResponseMessageSender signupRequestSender;
    private final MemberSignupResponseMessageSender assignmentMessageSender;
    private final MemberSignupResponseMessageSender checkinRequestMessageSender;

    public TwilioNotificationService(
            TwilioBackend config,
            MemberSignupResponseService service,
            SignupService signupService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService,
            AssignmentSetService assignmentSetService,
            AssignmentService assignmentService
    ) {
        this.signupRequestSender = new SignupRequestMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService
        );

        this.assignmentMessageSender = new AssignmentMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService
        );

        this.checkinRequestMessageSender = new CheckinRequestMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService
        );
    }

    @Override
    public NotificationResult sendSignupRequest(String signupId) {
        return signupRequestSender.sendMessages(signupId);
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
