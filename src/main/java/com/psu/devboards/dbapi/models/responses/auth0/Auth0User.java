package com.psu.devboards.dbapi.models.responses.auth0;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Auth0User {

    @JsonProperty("created_at")
    private String createdAt;

    private String email;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    private List<Identity> identities;

    private String name;

    private String nickname;

    private String picture;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("last_login")
    private String lastLogin;

    @JsonProperty("last_ip")
    private String lastIp;

    @JsonProperty("logins_count")
    private Integer loginsCount;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("given_name")
    private String givenName;

    private String locale;
}