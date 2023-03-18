package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.NotificationResult;

public interface NotificationService {
    NotificationResult sendSignupRequest(String signupId);

    NotificationResult sendAssignments(String signupId);

    NotificationResult sendCheckinRequest(String signupId);
}
