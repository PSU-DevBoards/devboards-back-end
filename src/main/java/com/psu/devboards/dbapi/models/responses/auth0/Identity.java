package com.psu.devboards.dbapi.models.responses.auth0;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Identity {

    private String connection;

    private String provider;

    @JsonProperty("user_id")
    private String userId;

    private Boolean isSocial;
}