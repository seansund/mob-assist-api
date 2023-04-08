package com.dex.mobassist.server.service.mock;

import com.dex.mobassist.server.cargo.NotificationResultCargo;
import com.dex.mobassist.server.model.NotificationResult;
import com.dex.mobassist.server.service.NotificationService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("sms-mock")
public class NotificationServiceMock implements NotificationService {
    @Override
    public NotificationResult sendSignupRequest(String signupId) {
        return new NotificationResultCargo("SignupRequest");
    }

    @Override
    public NotificationResult sendAssignments(String signupId) {
        return new NotificationResultCargo("Assignment");
    }

    @Override
    public NotificationResult sendCheckinRequest(String signupId) {
        return new NotificationResultCargo("Checkin");
    }
}
