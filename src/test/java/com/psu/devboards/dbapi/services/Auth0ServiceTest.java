package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.responses.auth0.Auth0User;
import com.psu.devboards.dbapi.models.responses.auth0.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Auth0ServiceTest {

    @Mock
    RestTemplateBuilder restTemplateBuilder;

    @Mock
    RestTemplate restTemplate;

    Auth0Service auth0Service;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        auth0Service = new Auth0Service(restTemplateBuilder);

        ReflectionTestUtils.setField(auth0Service, "clientId", "client-id");
        ReflectionTestUtils.setField(auth0Service, "clientSecret", "client-secret");
        ReflectionTestUtils.setField(auth0Service, "audience", "audience");
        ReflectionTestUtils.setField(auth0Service, "authenticationBaseUrl", "authentication");
        ReflectionTestUtils.setField(auth0Service, "managementBaseUrl", "management");
    }

    @Test
    void findUserById() {
        ResponseEntity<TokenResponse> response =
                new ResponseEntity<>(TokenResponse.builder().accessToken("access").build(), HttpStatus.OK);
        when(restTemplate.exchange(eq("authentication/oauth/token"), eq(HttpMethod.POST), any(),
                ArgumentMatchers.<Class<TokenResponse>>any())).thenReturn(response);

        ResponseEntity<Auth0User> userResponse = new ResponseEntity<>(Auth0User.builder().build(), HttpStatus.OK);
        when(restTemplate.exchange(eq("management/users/test"), eq(HttpMethod.GET), any(),
                ArgumentMatchers.<Class<Auth0User>>any())).thenReturn(userResponse);

        auth0Service.findUserById("test");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("access");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        verify(restTemplate, times(1))
                .exchange("management/users/test", HttpMethod.GET, requestEntity, Auth0User.class);
    }
}