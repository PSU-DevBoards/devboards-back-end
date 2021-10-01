package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.requests.OrganizationRequest;
import com.psu.devboards.dbapi.services.OrganizationService;
import com.psu.devboards.dbapi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {

    @Mock
    UserService userService;

    @Mock
    OrganizationService organizationService;

    @InjectMocks
    OrganizationController organizationController;

    Principal principal;
    User user;
    OrganizationRequest organizationRequest;

    @BeforeEach
    void setUp() {
        principal = mock(Principal.class);
        user = new User("testUser");
        organizationRequest = new OrganizationRequest("newOrganization");

        when(principal.getName()).thenReturn(user.getUsername());
        when(userService.findByUsername(user.getUsername())).thenReturn(user);
    }

    @Test
    void shouldCreateOrganization() {
        organizationController.postOrganization(organizationRequest, principal);

        verify(organizationService, times(1))
                .createOrganization(user, organizationRequest.getName());
    }

    @Test
    void shouldUpdateOrganization() {
        organizationController.patchOrganization(1, organizationRequest, principal);

        verify(organizationService, times(1))
                .updateOrganization(user, 1, organizationRequest);
    }

    @Test
    void shouldGetOrganization() {
        organizationController.getOrganization(1, principal);

        verify(organizationService, times(1)).findOrganization(user, 1);
    }

    @Test
    void shouldDeleteOrganization() {
        organizationController.deleteOrganization(1, principal);

        verify(organizationService, times(1)).deleteOrganization(user, 1);
    }
}