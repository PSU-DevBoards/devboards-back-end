package com.psu.devboards.dbapi.models.requests;

public interface OrganizationUserRequest {

    String getEmail();

    Integer getRoleId();

    Integer getOrganizationId();
}
