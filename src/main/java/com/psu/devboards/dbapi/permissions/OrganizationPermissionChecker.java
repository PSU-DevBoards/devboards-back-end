package com.psu.devboards.dbapi.permissions;

import com.psu.devboards.dbapi.models.entities.Organization;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Permission checker for Organization resources.
 */
@Component
public class OrganizationPermissionChecker extends PermissionChecker {

    public OrganizationPermissionChecker() {
        this.domainClassName = Organization.class.getSimpleName();
    }

    @Override
    protected Integer getOrganizationId(Serializable targetId) {
        return (Integer) targetId;
    }
}
