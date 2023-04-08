package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.cargo.TwilioWebhookRequestCargo;
import com.dex.mobassist.server.service.twilio.TwilioWebhookService;
import com.twilio.twiml.TwiML;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.print.attribute.standard.Media;

@Controller
@CrossOrigin
@Profile("sms-twilio")
public class TwilioWebhookController {
    private final TwilioWebhookService service;

    public TwilioWebhookController(TwilioWebhookService service) {
        this.service = service;
    }

    @PostMapping(
            path = "/twilio",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseBody
    public String twilioMessageWebhook(TwilioWebhookRequestCargo request) {
        System.out.println("Got message: " + request);

        return service.handleMessageWebhook(request).toXml();
    }

}
