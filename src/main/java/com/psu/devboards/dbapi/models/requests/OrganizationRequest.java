package com.psu.devboards.dbapi.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRequest {
    @NotBlank(message="Organization name cannot be blank.")
    @NotNull(message="Organization name must be defined.")
    private String name;
}
