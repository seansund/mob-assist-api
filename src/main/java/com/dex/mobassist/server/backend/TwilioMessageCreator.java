package com.dex.mobassist.server.backend;

import com.twilio.base.Resource;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioMessageCreator implements MessageCreator {
    @Override
    public Message createMessage(PhoneNumber to, PhoneNumber from, String message) {
        return Message.creator(to, from, message).create();
    }
}
