package org.emmek.beu2w3d2.config;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.emmek.beu2w3d2.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailSender {
    private final String apikey;
    private final String sender;

    public EmailSender(@Value("${sendgrid.apikey}") String apikey,
                       @Value("${sengrid.sender}") String sender) {
        this.apikey = apikey;
        this.sender = sender;
    }

    public void sendRegistrationEmail(User user) throws IOException {

        Email from = new Email(sender);
        String subject = "\n" + "Registration successful!";
        Email to = new Email(user.getEmail());
        Content content = new Content("text/plain",
                "Welcome " + user.getUsername() + " registration confirmed!");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apikey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sg.api(request);
    }
}