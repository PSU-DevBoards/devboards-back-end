package com.psu.devboards.dbapi.models.requests;

import com.psu.devboards.dbapi.models.requests.validators.NullOrNotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationPatchRequest implements OrganizationRequest {

    @NullOrNotBlank
    private String name;
}
