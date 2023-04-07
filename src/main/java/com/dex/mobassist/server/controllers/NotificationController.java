package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.NotificationResult;
import com.dex.mobassist.server.service.NotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class NotificationController {
    private final NotificationService service;

    public NotificationController(@Qualifier("NotificationService") NotificationService service) {
        this.service = service;
    }

    @MutationMapping
    public NotificationResult sendSignupRequest(@Argument("id") String signupId) {
        return service.sendSignupRequest(signupId);
    }

    @MutationMapping
    public NotificationResult sendSignupAssignments(@Argument("id") String signupId) {
        return service.sendAssignments(signupId);
    }

    @MutationMapping
    public NotificationResult sendSignupCheckin(@Argument("id") String signupId) {
        return service.sendCheckinRequest(signupId);
    }
}
