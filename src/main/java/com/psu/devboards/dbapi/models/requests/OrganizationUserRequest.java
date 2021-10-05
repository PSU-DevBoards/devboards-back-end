package com.psu.devboards.dbapi.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationUserRequest {

    @JsonProperty("user_id")
    private Integer userId;
}
