package com.dex.mobassist.server.service.gmail;

import com.dex.mobassist.server.backend.EmailNotificationConfig;
import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.MemberRef;
import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.service.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Properties;

public abstract class AbstractMemberSignupResponseEmailMessageSender extends AbstractMemberSignupResponseMessageSender<EmailNotificationConfig> {

    public AbstractMemberSignupResponseEmailMessageSender(
            EmailNotificationConfig config,
            MemberSignupResponseService service,
            SignupService signupService,
            SignupOptionSetService signupOptionSetService,
            SignupOptionService signupOptionService,
            AssignmentSetService assignmentSetService,
            AssignmentService assignmentService,
            MemberService memberService
    ) {
        super(config, service, signupService, signupOptionSetService, signupOptionService, assignmentSetService, assignmentService, memberService);
    }

    protected abstract String buildSubject(Signup signup);

    protected String getToEmail(@NonNull MemberRef member, @NonNull List<? extends Member> members) {
        return members.stream()
                .filter(mem -> member.getId().equals(member.getId()))
                .findFirst()
                .map(Member::getEmail)
                .orElse(null);
    }

    protected Message sendEmail(String toEmailAddress, String messageSubject, String bodyText) {
        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
                    .createScoped(GmailScopes.GMAIL_SEND);
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

            // Create the gmail API client
            Gmail service = new Gmail.Builder(new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    requestInitializer)
                    .setApplicationName("MobAssist")
                    .build();

            // Encode as MIME message
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(config.getFromAddress()));
            email.addRecipient(jakarta.mail.Message.RecipientType.TO,
                    new InternetAddress(toEmailAddress));
            email.setSubject(messageSubject);
            email.setText(bodyText);

            // Encode and wrap the MIME message into a gmail message
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
            Message message = new Message();
            message.setRaw(encodedEmail);

            // Create send message
            message = service.users().messages().send("me", message).execute();
            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
            return message;
        } catch (Exception e) {
            System.err.println("Error sending mail: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
