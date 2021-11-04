package com.psu.devboards.dbapi.permissions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PermissionEvaluatorImplTest {

    PermissionEvaluatorImpl permissionEvaluatorImpl;

    @BeforeEach
    void setUp() {
        permissionEvaluatorImpl = new PermissionEvaluatorImpl(null, null, null);
    }

    @Test
    void hasPermissionByObjectAlwaysReturnsFalse() {
        assertFalse(permissionEvaluatorImpl.hasPermission(null, new Object(), "A"));
    }

    @Test
    void hasPermissionPermissionByIdThrowsIfCheckerNotFound() {
        assertThrows(UnsupportedOperationException.class, () ->
                permissionEvaluatorImpl.hasPermission(null, 1, "Test", "A"));
    }
}