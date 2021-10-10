package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.requests.OrganizationRequest;
import com.psu.devboards.dbapi.repositories.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    OrganizationRepository organizationRepository;

    @InjectMocks
    OrganizationService organizationService;

    User user;
    User otherUser;
    Organization organization;

    @BeforeEach
    void setUp() {
        user = new User("testUser");
        otherUser = new User("otherUser");
        organization = new Organization(1, "newOrganization", user);
        organization.setUsers(new HashSet<>(Arrays.asList(new OrganizationUser(organization, user),
                new OrganizationUser(organization, otherUser))));
    }

    @Test
    void shouldCreateOrganization() {
        organization.setId(null);
        organizationService.createOrganization(user, organization.getName());

        verify(organizationRepository, times(1)).save(organization);
    }

    @Test
    void shouldUpdateOrganizationIfUserIsOwner() {
        Organization expected = new Organization(1, "newName", user);
        expected.setUsers(new HashSet<>(Collections.singletonList(new OrganizationUser(expected, user))));
        when(organizationRepository.findById(organization.getId())).thenReturn(Optional.ofNullable(organization));

        organizationService.updateOrganizationById(user, organization.getId(), new OrganizationRequest("newName"));

        verify(organizationRepository, times(1)).save(expected);
    }

    @Test
    void updateShouldThrow404IfOrganizationNotFound() {
        OrganizationRequest request = new OrganizationRequest("newName");
        when(organizationRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationService.updateOrganizationById(user, 1, request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void updateShouldThrow403IfUserDoesNotHavePermission() {
        OrganizationRequest request = new OrganizationRequest("newName");
        organization.setOwner(null);
        when(organizationRepository.findById(organization.getId())).thenReturn(Optional.ofNullable(organization));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationService.updateOrganizationById(user, organization.getId(), request));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void shouldDeleteOrganizationIfUserIsOwner() {
        when(organizationRepository.findById(organization.getId())).thenReturn(Optional.ofNullable(organization));

        organizationService.deleteOrganizationById(user, organization.getId());

        verify(organizationRepository, times(1)).delete(organization);
    }

    @Test
    void deleteShouldThrow404IfOrganizationNotFound() {
        when(organizationRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationService.deleteOrganizationById(user, 1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void deleteShouldThrow403IfUserDoesNotHavePermission() {
        organization.setOwner(null);
        when(organizationRepository.findById(organization.getId())).thenReturn(Optional.ofNullable(organization));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationService.deleteOrganizationById(user, organization.getId()));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void shouldFindOrganizationIfUserIsInOrg() {
        when(organizationRepository.findById(organization.getId())).thenReturn(Optional.ofNullable(organization));

        organizationService.findOrganizationById(user, organization.getId());

        verify(organizationRepository, times(1)).findById(organization.getId());
    }

    @Test
    void findShouldThrow404IfOrganizationNotFound() {
        when(organizationRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationService.findOrganizationById(user, 1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void findShouldThrow403IfUserDoesNotHavePermission() {
        organization.setOwner(null);
        organization.setUsers(Collections.emptySet());
        when(organizationRepository.findById(organization.getId())).thenReturn(Optional.ofNullable(organization));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationService.findOrganizationById(user, organization.getId()));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }
}