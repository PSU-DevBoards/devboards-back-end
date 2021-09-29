package com.psu.devboards.dbapi.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridService {

    private final SendGrid sendGrid;
    private final Email from;

    public SendGridService(@Value("${spring.sendgrid.api-key}") final String apiKey,
                           @Value("${email.from}") final String fromEmail) {
        this.sendGrid = new SendGrid(apiKey);
        this.from = new Email(fromEmail);
    }

    public void sendEmail(String toEmail, String subject, String message) throws IOException {
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        sendGrid.api(request);
    }
}
