package com.psu.devboards.dbapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class SendGridServiceTest {

    @Autowired
    SendGridService sendGridService;

    @Test
    void shouldCallSendgridAPI() throws IOException {

        sendGridService.sendEmail("test@test.com", "Test", "Test");
    }
}