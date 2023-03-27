package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.TwilioWebhookRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TwilioWebhookRequestCargo implements TwilioWebhookRequest {
    @JsonProperty("MessageSid")
    private String messageSid;
    @JsonProperty("SmsSid")
    private String smsSid;
    @JsonProperty("AccountSid")
    private String accountSid;
    @JsonProperty("MessagingServiceSid")
    private String messagingServiceSid;
    @JsonProperty("From")
    private String from;
    @JsonProperty("To")
    private String to;
    @JsonProperty("Body")
    private String body;
    @JsonProperty("NumMedia")
    private Integer numMedia;
}
