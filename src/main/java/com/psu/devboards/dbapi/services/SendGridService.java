package com.psu.devboards.dbapi.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SendGridService {

    private final SendGrid sendGrid;
    private final Email from;
    @Value("${email.template-ids.org-invite}")
    private String orgInviteTemplateId;

    public SendGridService(@Value("${spring.sendgrid.api-key}") final String apiKey,
                           @Value("${email.from}") final String fromEmail) {
        this.sendGrid = new SendGrid(apiKey);
        this.from = new Email(fromEmail);
    }

    public void sendOrgInviteEmail(String toEmail, String organizationName, String organizationId) {
        Map<String, String> templateData = new HashMap<>();
        templateData.put("organization_name", organizationName);
        templateData.put("organization_id", organizationId);

        sendTemplateEmail(orgInviteTemplateId, toEmail, templateData);
    }

    private void sendTemplateEmail(String templateId, String toEmail, Map<String, String> templateData) {

        Personalization personalization = new Personalization();
        templateData.forEach(personalization::addDynamicTemplateData);

        Email to = new Email(toEmail);
        personalization.addTo(to);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(templateId);
        mail.addPersonalization(personalization);

        sendEmailRequest(mail);
    }

    private void sendEmailRequest(Mail mail) {
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            sendGrid.api(request);

            log.info("Email sent to {} through Send Grid.",
                    mail.getPersonalization().get(0).getTos().get(0).getEmail());
        } catch (Exception ex) {
            log.error("Error sending email to {} through Send Grid, {}",
                    mail.getPersonalization().get(0).getTos().get(0).getEmail(), ex.getMessage());
        }
    }
}
