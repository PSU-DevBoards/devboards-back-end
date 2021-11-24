package com.psu.devboards.dbapi.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class OrganizationUserFullRequest implements OrganizationUserRequest {

    @NotNull
    private String email;

    @NotNull
    private Integer roleId;

    @JsonIgnore
    private Integer organizationId;

    public OrganizationUserFullRequest(String email, Integer roleId) {
        this.email = email;
        this.roleId = roleId;
    }
}
