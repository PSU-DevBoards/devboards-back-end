package com.psu.devboards.dbapi.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationUserRequest {

    @NotNull
    private String email;

    @NotNull
    @JsonProperty("roleId")
    private Integer roleId;

    @JsonIgnore
    private Integer organizationId;

    public OrganizationUserRequest(String email, Integer roleId) {
        this.email = email;
        this.roleId = roleId;
    }
}
