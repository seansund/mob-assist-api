package com.dex.mobassist.server.backend;

public interface TwilioConfig extends NotificationConfig {
    String getPhoneNumber();

    String getAccountSid();

    String getAuthToken();
}
