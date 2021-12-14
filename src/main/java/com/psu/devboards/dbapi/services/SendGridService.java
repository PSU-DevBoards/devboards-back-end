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

/**
 * Service for interacting with the SendGrid API and sending customer communications.
 *
 * @see <a href="https://github.com/sendgrid/sendgrid-java">SendGrid Java API Documentation</a>
 */
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

    /**
     * Sends an organization invitation welcome email.
     *
     * @param toEmail          The email to invite.
     * @param organizationName The name of the organization the email is being invited to.
     * @param organizationId   The id of the organization the email is being invited to.
     */
    public void sendOrgInviteEmail(String toEmail, String organizationName, String organizationId) {
        Map<String, String> templateData = new HashMap<>();
        templateData.put("organization_name", organizationName);
        templateData.put("organization_id", organizationId);

        sendTemplateEmail(orgInviteTemplateId, toEmail, templateData);
    }

    /**
     * @param templateId   The sendgrid dynamic template id.
     * @param toEmail      The email to send the communication to.
     * @param templateData A map of data for the template, keys of the map should correlate to template variables.
     */
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

    /**
     * Sends an email request to the SendGrid API.
     *
     * @param mail The created mail object.
     */
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
