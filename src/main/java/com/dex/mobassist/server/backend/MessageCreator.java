package com.dex.mobassist.server.backend;

import com.twilio.base.Resource;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public interface MessageCreator {
    Message createMessage(PhoneNumber to, PhoneNumber from, String message);
}
