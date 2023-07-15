package com.dex.mobassist.server.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("sms-mock")
public class MockSmsBackend {
    @Bean
    public MessageCreator messageCreator() {
        System.out.println("############# Mock message creator #############");
        return new MockMessageCreator();
    }
}
