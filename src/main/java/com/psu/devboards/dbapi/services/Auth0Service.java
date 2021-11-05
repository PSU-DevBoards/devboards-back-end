package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.responses.auth0.Auth0User;
import com.psu.devboards.dbapi.models.responses.auth0.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class Auth0Service {

    private final RestTemplate restTemplate;

    @Value("${auth0.management.client-id}")
    private String clientId;

    @Value("${auth0.management.client-secret}")
    private String clientSecret;

    @Value("${auth0.management.audience}")
    private String audience;

    @Value("https://${auth0.domain}")
    private String authenticationBaseUrl;

    @Value("https://${auth0.domain}/api/v2")
    private String managementBaseUrl;

    public Auth0Service(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Auth0User findUserById(String userId) throws NullPointerException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Auth0User> response = restTemplate.exchange(managementBaseUrl + "/users/" + userId,
                HttpMethod.GET, requestEntity, Auth0User.class);

        return Objects.requireNonNull(response.getBody());
    }

    private String getAccessToken() throws NullPointerException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("audience", audience);

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<TokenResponse> response = restTemplate
                .exchange(authenticationBaseUrl + "/oauth/token", HttpMethod.POST, formEntity, TokenResponse.class);

        return Objects.requireNonNull(response.getBody()).getAccessToken();
    }
}
