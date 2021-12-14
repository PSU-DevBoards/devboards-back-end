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

/**
 * Service for interacting with the Auth0 APIs.
 *
 * @see <a href="https://auth0.com/docs/api/management/v2">Auth0 Management API V2 Docs</a>
 */
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

    /**
     * Finds a user by their Auth0 id with the auth0 management API.
     *
     * @param userId The Auth0 unique id of the user.
     * @return The located Auth0 user record.
     */
    public Auth0User findUserById(String userId) throws NullPointerException, IllegalArgumentException {
        // Ensure that the ID matches the known format to avoid any SSRF.
        // https://owasp.org/www-community/attacks/Server_Side_Request_Forgery
        if (!userId.matches(("^[-a-z0-9|]+$")))
            throw new IllegalArgumentException("User ID must match the pattern ^[-a-z0-9|]+$");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Auth0User> response = restTemplate.exchange(managementBaseUrl + "/users/" + userId,
                HttpMethod.GET, requestEntity, Auth0User.class);

        return Objects.requireNonNull(response.getBody());
    }

    /**
     * Gets the access token required to interact with the Auth0 management API.
     *
     * @return The access token.
     */
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
