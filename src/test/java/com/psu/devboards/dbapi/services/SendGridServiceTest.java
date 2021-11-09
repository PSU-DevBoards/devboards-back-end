package com.psu.devboards.dbapi.services;

import com.sendgrid.SendGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendGridServiceTest {

    @Mock
    SendGrid sendGrid;

    SendGridService sendGridService;

    @BeforeEach
    void setUp() {
        sendGridService = new SendGridService("api-key", "from@email.com");

        ReflectionTestUtils.setField(sendGridService, "sendGrid", sendGrid);
        ReflectionTestUtils.setField(sendGridService, "orgInviteTemplateId", "org-invite-template-id");
    }

    @Test
    void shouldCallSendgridAPI() throws IOException {
        sendGridService.sendOrgInviteEmail("to@email.com", "name", "id");
        verify(sendGrid, times(1)).api(argThat(s ->
                s.getBody().equals("{\"from\":{\"email\":\"from@email.com\"}," +
                        "\"personalizations\":[{\"to\":[{\"email\":\"to@email.com\"}]," +
                        "\"dynamic_template_data\":{\"organization_id\":\"id\",\"organization_name\":\"name\"}}]," +
                        "\"template_id\":\"org-invite-template-id\"}")
        ));
    }

    @Test
    void shouldCatchSendgridError() throws IOException {
        when(sendGrid.api(any())).thenThrow(new IOException());

        try {
            sendGridService.sendOrgInviteEmail("to@email.com", "name", "id");
        } catch (Exception ex) {
            fail("Exception should not be thrown.");
        }
    }
}