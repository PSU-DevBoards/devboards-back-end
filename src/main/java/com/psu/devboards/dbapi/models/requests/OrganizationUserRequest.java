package com.psu.devboards.dbapi.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationUserRequest {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("role_id")
    private Integer roleId;
}
