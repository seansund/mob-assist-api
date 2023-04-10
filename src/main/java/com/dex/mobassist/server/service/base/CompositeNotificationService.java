package com.dex.mobassist.server.service.base;

import com.dex.mobassist.server.model.NotificationResult;
import com.dex.mobassist.server.service.NotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("NotificationService")
public class CompositeNotificationService implements NotificationService {
    final private NotificationService emailService;
    final private NotificationService textService;

    public CompositeNotificationService(
            @Qualifier("EmailNotification") NotificationService emailService,
            @Qualifier("SMSNotification") NotificationService textService
    ) {
        this.emailService = emailService;
        this.textService = textService;
    }

    @Override
    public NotificationResult sendSignupRequest(String signupId) {
        return emailService.sendSignupRequest(signupId)
                .withResult(textService.sendSignupRequest(signupId));
    }

    @Override
    public NotificationResult sendSignupRequestToNoResponse(String signupId) {
        return emailService.sendSignupRequestToNoResponse(signupId)
                .withResult(textService.sendSignupRequestToNoResponse(signupId));
    }

    @Override
    public NotificationResult sendAssignments(String signupId) {
        return emailService.sendAssignments(signupId)
                .withResult(textService.sendAssignments(signupId));
    }

    @Override
    public NotificationResult sendCheckinRequest(String signupId) {
        return emailService.sendCheckinRequest(signupId)
                .withResult(textService.sendCheckinRequest(signupId));
    }
}
