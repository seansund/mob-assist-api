package com.dex.mobassist.server.backend;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class MockMessageCreator implements MessageCreator {
    @Override
    public Message createMessage(PhoneNumber to, PhoneNumber from, String message) {
        System.out.printf("Message: {to: %s, from: %s, msg: %s}%n", to, from, message);
        return null;
    }
}
