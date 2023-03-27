package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.cargo.TwilioWebhookRequestCargo;
import com.dex.mobassist.server.service.twilio.TwilioWebhookService;
import com.twilio.twiml.TwiML;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Profile("twilio")
public class TwilioWebhookController {
    private final TwilioWebhookService service;

    public TwilioWebhookController(TwilioWebhookService service) {
        this.service = service;
    }

    @PostMapping("/twilio")
    @ResponseBody
    public TwiML twilioMessageWebhook(@RequestBody TwilioWebhookRequestCargo request) {
        return service.handleMessageWebhook(request);
    }

}
