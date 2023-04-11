package com.dex.mobassist.server.service.gmail;

import com.dex.mobassist.server.backend.EmailNotificationConfig;
import com.dex.mobassist.server.model.NotificationResult;
import com.dex.mobassist.server.service.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service("EmailNotification")
@Profile("email-gmail")
public class GmailNotificationService implements NotificationService {
    private final MemberSignupResponseMessageSender signupRequestSender;
    private final MemberSignupResponseMessageSender signupRequestNoResponseSender;
    private final MemberSignupResponseMessageSender assignmentMessageSender;
    private final MemberSignupResponseMessageSender checkinRequestMessageSender;

    public GmailNotificationService(
            EmailNotificationConfig config,
            MemberSignupResponseService service,
            SignupService signupService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService,
            AssignmentSetService assignmentSetService,
            AssignmentService assignmentService,
            MemberService memberService
    ) {
        this.signupRequestSender = new SignupRequestMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService,
                memberService
        );

        this.signupRequestNoResponseSender = new SignupRequestNoResponseMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService,
                memberService
        );

        this.assignmentMessageSender = new AssignmentMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService,
                memberService
        );

        this.checkinRequestMessageSender = new CheckinRequestMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService,
                memberService
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
