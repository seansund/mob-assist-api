package com.dex.mobassist.server.service.twilio;

import com.dex.mobassist.server.cargo.AssignmentGroupCargo;
import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.*;
import com.dex.mobassist.server.util.Collections;
import com.dex.mobassist.server.util.Dates;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiML;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TwilioWebhookService {

    private final MemberSignupResponseService service;
    private final SignupService signupService;
    private final SignupOptionSetService signupOptionSetService;
    private final SignupOptionService signupOptionService;
    private final MemberService memberService;
    private final AssignmentService assignmentService;

    public TwilioWebhookService(
            MemberSignupResponseService service,
            SignupService signupService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService,
            MemberService memberService,
            AssignmentService assignmentService
    ) {
        this.service = service;
        this.signupService = signupService;
        this.signupOptionSetService = signupOptionSetService;
        this.signupOptionService = signupOptionService;
        this.memberService = memberService;
        this.assignmentService = assignmentService;
    }

    public TwiML handleMessageWebhook(TwilioWebhookRequest request) {
        // Accepted responses:
        // "Yes" - If not already signed up, "Great, which service?"

        // "Yes" - If already signed up (not a No response) and not in a check-in window, respond with "Thank you"
        // "Yes" - If already signed up (not a No response) and in check-in window, check in
        // Decline option - Change response to "No" or create "No" response and send something like "Got it"
        // Other option - Change response, if in checkin window checkin?
        // Something else - "Sorry I didn't understand your response"

        final String memberPhone = request.getFrom().replaceFirst("^\\+1", "");

        final Signup signup = signupService.getCurrent();
        final List<? extends SignupOption> signupOptions = getSignupOptions(signup);

        final Optional<? extends MemberSignupResponse> response = service.getSignupResponseForUser(signup.getId(), memberPhone);
        final Optional<? extends SignupOption> currentSelection = response.map(MemberSignupResponse::getSelectedOption).map(this::getSignupOption);

        String body = "";
        // TODO can we determine context of response? If in conversation perhaps?
        if (replyIsStop(request)) {
            memberService.setPreferredContact(memberPhone, "none");

            body = "Your preferred contact has been changed. You will receive no further texts.";
        } else if (replyIsOptions(request)) {
            body = buildHelpResponse(signupOptions);
        } else if (replyIsShow(request) && response.isEmpty()) {
            body = String.format(
                    "The next %s is %s. You have not yet responded. Options are %s.",
                    signup.getTitle(),
                    new SimpleDateFormat("MM/dd/yyyy").format(signup.getDate()),
                    getOptionList(signupOptions)
            );
        } else if (replyIsShow(request) && response.isPresent()) {
            final MemberSignupResponse responseVal = response.get();

            final String assignment = Collections.isNullOrEmpty(responseVal.getAssignments())
                    ? ""
                    : String.format(" and assigned to %s", formatAssignment(responseVal.getAssignments()));

            body = String.format(
                    "The next %s is %s. You are signed up for %s%s.",
                    signup.getTitle(),
                    new SimpleDateFormat("MM/dd/yyyy").format(signup.getDate()),
                    getSignupOption(responseVal.getSelectedOption()).getValue(),
                    assignment
            );
        } else if (replyIsYes(request) && response.isEmpty()) {
            body = "Thank you for your response. Which option? " + getOptionList(signupOptions, false);
        } else if (replyIsYes(request) && isEligibleForCheckin(signup, currentSelection)) {
            service.checkIn(response.get().getId());

            body = "You are checked in for " + currentSelection.get().getValue();
        } else if (replyIsYes(request) && !isEligibleForCheckin(signup, currentSelection)) {
            body = "We are not checking in yet. Try again later.";
        } else {
            final Optional<? extends SignupOption> selectedOption = getMatchingOption(signupOptions, request.getBody());

            if (selectedOption.isPresent()) {
                service.signUp(signup, memberPhone, selectedOption.get().getValue());

                body = "Thank you for your response.";
                if (isEligibleForCheckin(signup, selectedOption)) {
                    body += " Reply Yes to check in.";
                }
            } else {
                body = "I didn't understand your response. " + buildHelpResponse(signupOptions);;
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

    protected String formatAssignment(List<? extends AssignmentRef> refs) {
        final String assignmentDisplay = AssignmentMessageSender.buildAssignmentDisplay(loadAssignments(refs));
        final String assignmentUrl = AssignmentMessageSender.buildAssignmentDiagramUrl(assignmentDisplay);

        return String.format(
                "%s %s",
                assignmentDisplay,
                assignmentUrl
        );
    }

    protected Function<List<String>, String> joiningComma(@NonNull String lastSeparator) {

        return list -> {
            if (list.size() <= 1) {
                return String.join("", list);
            }

            if (list.size() == 2) {
                return String.join(String.format(" %s ", lastSeparator), list);
            }

            final int lastIndex = list.size() - 1;
            return String.join(
                    String.format(", %s ", lastSeparator),
                    String.join(", ", list.subList(0, lastIndex)),
                    list.get(lastIndex)
            );
        };
    }

    protected String getOptionList(List<? extends SignupOption> signupOptions) {
        return getOptionList(signupOptions, true);
    }

    protected String getOptionList(List<? extends SignupOption> signupOptions, boolean includeDecline) {

        return signupOptions.stream()
                .filter(option -> includeDecline || !Boolean.TRUE.equals(option.getDeclineOption()))
                .map(SignupOption::getShortName)
                .map(String::toUpperCase)
                .collect(Collectors.collectingAndThen(Collectors.toList(), joiningComma("or")));
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

    protected boolean replyIsShow(@NonNull TwilioWebhookRequest request) {
        return "show".equalsIgnoreCase(request.getBody().trim());
    }

    protected boolean replyIsOptions(@NonNull TwilioWebhookRequest request) {
        return "options".equalsIgnoreCase(request.getBody().trim()) || "option".equalsIgnoreCase(request.getBody().trim());
    }

    protected boolean replyIsStop(@NonNull TwilioWebhookRequest request) {
        return "stop".equalsIgnoreCase(request.getBody().trim());
    }

    protected boolean replyIsYes(@NonNull TwilioWebhookRequest request) {
        return "yes".equalsIgnoreCase(request.getBody().trim());
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

    protected List<? extends Assignment> loadAssignments(List<? extends AssignmentRef> assignmentRefs) {
        if (assignmentRefs.stream().allMatch(ref -> ref instanceof Assignment)) {
            return (List<? extends Assignment>) assignmentRefs;
        }

        return assignmentService.findAllById(assignmentRefs.stream().map(ModelRef::getId).toList());
    }

    protected String buildHelpResponse(List<? extends SignupOption> signupOptions) {
        return String.format(
                "Replies to change signup response are: %s. Reply SHOW to get your response for the current sign up. Reply YES or CHECKIN to check in the day of the event. Reply OPTIONS to get more information. Reply STOP to unsubscribe.",
                getOptionList(signupOptions)
        );
    }
}
