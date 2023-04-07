package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.NotificationResult;

public interface MemberSignupResponseMessageSender {
    NotificationResult sendMessages(String signupId);
}
