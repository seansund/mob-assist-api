package com.dex.mobassist.server.service.twilio;

import com.dex.mobassist.server.backend.TwilioConfig;
import com.dex.mobassist.server.cargo.AssignmentGroupCargo;
import com.dex.mobassist.server.cargo.NotificationResultCargo;
import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.service.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.twilio.rest.api.v2010.account.Message.creator;
import static java.lang.String.format;

@Service("NotificationService")
public class TwilioNotificationService implements NotificationService {

    private final MemberSignupResponseMessageSender signupRequestSender;
    private final MemberSignupResponseMessageSender assignmentMessageSender;
    private final MemberSignupResponseMessageSender checkinRequestMessageSender;

    public TwilioNotificationService(
            TwilioConfig config,
            MemberSignupResponseService service,
            SignupService signupService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService,
            AssignmentSetService assignmentSetService,
            AssignmentService assignmentService
    ) {
        this.signupRequestSender = new SignupRequestMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService
        );

        this.assignmentMessageSender = new AssignmentMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService
        );

        this.checkinRequestMessageSender = new CheckinRequestMessageSender(
                config,
                service,
                signupService,
                signupOptionSetService,
                signupOptionService,
                assignmentSetService,
                assignmentService
        );
    }

    @Override
    public NotificationResult sendSignupRequest(String signupId) {
        return signupRequestSender.sendMessages(signupId);
    }

    @Override
    public NotificationResult sendAssignments(String signupId) {
        return assignmentMessageSender.sendMessages(signupId);
    }

    @Override
    public NotificationResult sendCheckinRequest(String signupId) {
        return checkinRequestMessageSender.sendMessages(signupId);
    }
}
