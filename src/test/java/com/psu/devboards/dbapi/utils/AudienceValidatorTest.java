package com.psu.devboards.dbapi.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class AudienceValidatorTest {

    AudienceValidator audienceValidator;

    @BeforeEach
    void setUp() {
        audienceValidator = new AudienceValidator("test");
    }

    @Test
    void shouldReturnSuccessForCorrectAudience() {
        Jwt jwt = new Jwt("a", Instant.MIN, Instant.now(), Collections.singletonMap("foo", "bar"),
                Collections.singletonMap("aud", "test"));

        OAuth2TokenValidatorResult result = audienceValidator.validate(jwt);
        assertFalse(result.hasErrors());
    }

    @Test
    void shouldReturnFailureForIncorrectAudience() {
        Jwt jwt = new Jwt("a", Instant.MIN, Instant.now(), Collections.singletonMap("foo", "bar"),
                Collections.singletonMap("aud", "wrong"));

        OAuth2TokenValidatorResult result = audienceValidator.validate(jwt);
        assertTrue(result.hasErrors());
    }
}