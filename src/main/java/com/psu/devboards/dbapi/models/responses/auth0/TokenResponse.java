package com.psu.devboards.dbapi.models.responses.auth0;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    private String scope;

    @JsonProperty("expires_int")
    private Integer expiresIn;

    @JsonProperty("token_type")
    private String tokenType;
}
