package com.psu.devboards.dbapi.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationUserRequest {

    @NotNull
    @JsonProperty("user_id")
    private Integer userId;

    @NotNull
    @JsonProperty("role_id")
    private Integer roleId;

    @JsonIgnore
    private Integer organizationId;

    public OrganizationUserRequest(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
