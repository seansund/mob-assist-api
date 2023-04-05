package com.dex.mobassist.server.backend;

import com.twilio.Twilio;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Primary
public class TwilioBackend implements TwilioConfig {

    final private TwilioConfigData config;

    public TwilioBackend(TwilioConfigData config) {
        this.config = config;

        System.out.println("Initializing twilio config: " + config.getAccountSid() + ", " + config.getPhoneNumber());
        Twilio.init(config.getAccountSid(), config.getAuthToken());
    }

    public TwilioConfig getConfig() {
        return config;
    }

    @Override
    public String getPhoneNumber() {
        return config.getPhoneNumber();
    }

    @Override
    public String getAccountSid() {
        return config.getAccountSid();
    }

    @Override
    public String getAuthToken() {
        return config.getAuthToken();
    }
}
