package com.dex.mobassist.server.service.twilio;

import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.MemberSignupResponseService;
import com.dex.mobassist.server.service.SignupOptionService;
import com.dex.mobassist.server.service.SignupOptionSetService;
import com.dex.mobassist.server.service.SignupService;
import com.dex.mobassist.server.util.Dates;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiML;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TwilioWebhookService {

    private final MemberSignupResponseService service;
    private final SignupService signupService;
    private final SignupOptionSetService signupOptionSetService;
    private final SignupOptionService signupOptionService;

    public TwilioWebhookService(
            MemberSignupResponseService service,
            SignupService signupService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService
    ) {
        this.service = service;
        this.signupService = signupService;
        this.signupOptionSetService = signupOptionSetService;
        this.signupOptionService = signupOptionService;
    }

    public TwiML handleMessageWebhook(TwilioWebhookRequest request) {
        // Accepted responses:
        // "Yes" - If not already signed up, "Great, which service?"

        // "Yes" - If already signed up (not a No response) and not in a check-in window, respond with "Thank you"
        // "Yes" - If already signed up (not a No response) and in check-in window, check in
        // Decline option - Change response to "No" or create "No" response and send something like "Got it"
        // Other option - Change response, if in checkin window checkin?
        // Something else - "Sorry I didn't understand your response"

        final String memberPhone = request.getFrom();

        final Signup signup = signupService.getCurrent();
        final List<? extends SignupOption> signupOptions = getSignupOptions(signup);

        final Optional<? extends MemberSignupResponse> response = service.getSignupResponseForUser(signup.getId(), memberPhone);
        final Optional<? extends SignupOption> currentSelection = response.map(MemberSignupResponse::getSelectedOption).map(this::getSignupOption);

        String body = "";
        // TODO can we determine context of response? If in conversation perhaps?
        if (replyIsYes(request) && response.isEmpty()) {
            body = "Thank you for your response. Which option? " + getOptionList(signupOptions);
        } else if (replyIsYes(request) && isEligibleForCheckin(signup, currentSelection)) {
            service.checkIn(response.get().getId());

            body += "You are checked in for " + currentSelection.get().getValue();
        } else {
            final Optional<? extends SignupOption> selectedOption = getMatchingOption(signupOptions, request.getBody());

            if (selectedOption.isPresent()) {
                service.signUp(signup, memberPhone, selectedOption.get().getValue());

                body = "Thank you for your response.";
                if (isEligibleForCheckin(signup, selectedOption)) {
                    body += " Reply Yes to check in.";
                }
            } else {
                body = "I didn't understand your response. Valid responses are Yes, " + getOptionList(signupOptions);
            }
        }

        final Body b = new Body
                .Builder(body)
                .build();
        final Message sms = new Message
                .Builder()
                .body(b)
                .build();
        return new MessagingResponse
                .Builder()
                .message(sms)
                .build();
    }

    protected String getOptionList(List<? extends SignupOption> signupOptions) {
        return signupOptions.stream()
                .filter(option -> !Boolean.TRUE.equals(option.getDeclineOption())).map(SignupOption::getShortName)
                .collect(Collectors.joining(", "));
    }

    protected boolean isEligibleForCheckin(Signup signup, Optional<? extends SignupOption> selectedOption) {
        return selectedOption.filter(signupOption -> isEligibleForCheckin(signup, signupOption)).isPresent();

    }

    protected boolean isEligibleForCheckin(Signup signup, SignupOption selectedOption) {
        if (Boolean.TRUE.equals(selectedOption.getDeclineOption())) {
            return false;
        }

        return Dates.isToday(signup.getDate());
    }

    protected boolean replyIsYes(@NonNull TwilioWebhookRequest request) {
        return "yes".equalsIgnoreCase(request.getBody());
    }

    protected Optional<? extends SignupOption> getMatchingOption(List<? extends SignupOption> options, String optionValue) {
        return options.stream()
                .filter(option -> optionValue.equalsIgnoreCase(option.getShortName()) || optionValue.equalsIgnoreCase(option.getValue()))
                .findFirst();
    }

    protected List<? extends SignupOption> getSignupOptions(@NonNull Signup signup) {
        final SignupOptionSetRef optionSetRef = signup.getOptions();

        final List<? extends SignupOptionRef> optionRefs = (optionSetRef instanceof SignupOptionSet)
                ? ((SignupOptionSet)optionSetRef).getOptions()
                : Optional.of(signupOptionSetService.getById(optionSetRef.getId())).map(SignupOptionSet::getOptions).orElseGet(ArrayList::new);

        return getSignupOptions(optionRefs);
    }

    protected List<? extends SignupOption> getSignupOptions(@NonNull List<? extends SignupOptionRef> optionRefs) {
        if (optionRefs.stream().allMatch(ref -> ref instanceof SignupOption)) {
            return optionRefs.stream().map(ref -> (SignupOption) ref).toList();
        }

        return signupOptionService.findAllById(optionRefs.stream().map(ModelRef::getId).toList());
    }

    protected SignupOption getSignupOption(@NonNull SignupOptionRef ref) {
        if (ref instanceof SignupOption) {
            return (SignupOption) ref;
        }

        return signupOptionService.getById(ref.getId());
    }
}
