package com.dex.mobassist.server.backend;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class EmailNotificationConfig implements NotificationConfig {
    private String username;
    private String pasword;
    private String fromAddress;
    private String replyToAddress;

    @Override
    public String getChannel() {
        return "email";
    }
}
