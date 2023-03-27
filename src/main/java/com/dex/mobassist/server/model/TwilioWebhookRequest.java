package com.dex.mobassist.server.model;

public interface TwilioWebhookRequest {
    String getMessageSid();

    String getSmsSid();

    String getAccountSid();

    String getMessagingServiceSid();

    String getFrom();

    String getTo();

    String getBody();

    Integer getNumMedia();

    void setMessageSid(String messageSid);
    default <T extends TwilioWebhookRequest> T withMessageSid(String messageSid) {
        setMessageSid(messageSid);

        return (T) this;
    }

    void setSmsSid(String smsSid);

    default <T extends TwilioWebhookRequest> T withSmsSid(String smsSid) {
        setSmsSid(smsSid);

        return (T) this;
    }

    void setAccountSid(String accountSid);

    default <T extends TwilioWebhookRequest> T withAccountSid(String accountSid) {
        setAccountSid(accountSid);

        return (T) this;
    }

    void setMessagingServiceSid(String messagingServiceSid);
    default <T extends TwilioWebhookRequest> T withMessagingServiceSid(String messagingServiceSid) {
        setMessagingServiceSid(messagingServiceSid);

        return (T) this;
    }

    void setFrom(String from);

    default <T extends TwilioWebhookRequest> T withFrom(String from) {
        setFrom(from);

        return (T) this;
    }

    void setTo(String to);

    default <T extends TwilioWebhookRequest> T withTo(String to) {
        setTo(to);

        return (T) this;
    }

    void setBody(String body);

    default <T extends TwilioWebhookRequest> T withBody(String body) {
        setBody(body);

        return (T) this;
    }

    void setNumMedia(Integer numMedia);

    default <T extends TwilioWebhookRequest> T withNumMedia(Integer numMedia) {
        setNumMedia(numMedia);

        return (T) this;
    }
}
