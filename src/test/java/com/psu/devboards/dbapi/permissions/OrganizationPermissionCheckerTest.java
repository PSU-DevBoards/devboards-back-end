package com.psu.devboards.dbapi.permissions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class OrganizationPermissionCheckerTest {

    @Test
    void hasPermissionReturnsFalseWhenUnrecognizedPermission() {
        OrganizationPermissionChecker organizationPermissionChecker = new OrganizationPermissionChecker();

        assertFalse(organizationPermissionChecker.hasPermission(null, null, "a"));
    }
}