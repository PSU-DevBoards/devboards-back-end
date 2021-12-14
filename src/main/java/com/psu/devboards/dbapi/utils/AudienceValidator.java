package com.psu.devboards.dbapi.utils;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Validates the JWT token audience.
 *
 * @see OAuth2TokenValidator
 */
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private final String audience;

    public AudienceValidator(String audience) {
        this.audience = audience;
    }

    /**
     * Validates the JWT by determining if its audience contains the configured audience for this application.
     *
     * @param jwt The jwt to validate.
     * @return The token validation result. Success if it is valid and failure otherwise.
     */
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        OAuth2Error error = new OAuth2Error("invalid_token", "The required audience is missing", null);

        if (jwt.getAudience().contains(audience)) return OAuth2TokenValidatorResult.success();

        return OAuth2TokenValidatorResult.failure(error);
    }
}
