package com.dex.mobassist.server.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfigData implements TwilioConfig {
    @Value("${twilio.accountSid}")
    private String accountSid;
    @Value("${twilio.authToken}")
    private String authToken;
    @Value("${twilio.phoneNumber}")
    private String phoneNumber;

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }
    @Override
    public String getAccountSid() { return accountSid; }
    @Override
    public String getAuthToken() { return authToken; }

    @Override
    public String getChannel() {
        return "text";
    }
}
