package com.psu.devboards.dbapi.configs;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

public class TestSecurityConfig {
    static final String AUTH0_TOKEN = "token";
    static final String SUB = "sub";
    static final String AUTH0ID = "sms|12345678";

    public JwtDecoder jwtDecoder() {
        return new JwtDecoder() {
            @Override
            public Jwt decode(String token) {
                return jwt();
            }
        };
    }

    public Jwt jwt() {

        // This is a place to add general and maybe custom claims which should be available after parsing token in
        // the live system
        Map<String, Object> claims = Collections.singletonMap("sub", "auth0id");

        //This is an object that represents contents of jwt token after parsing
        return new Jwt(
                AUTH0_TOKEN,
                Instant.now(),
                Instant.now().plusSeconds(30),
                Collections.singletonMap("alg", "none"),
                claims
        );
    }
}
