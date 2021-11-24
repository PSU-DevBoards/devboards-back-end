package com.psu.devboards.dbapi.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationUserPatchRequest implements OrganizationUserRequest {

    private Integer roleId;

    @JsonIgnore
    private Integer organizationId;

    @Override
    public String getEmail() {
        throw new UnsupportedOperationException("Cannot retrieve email from OrganizationUserPatch request.");
    }
}
