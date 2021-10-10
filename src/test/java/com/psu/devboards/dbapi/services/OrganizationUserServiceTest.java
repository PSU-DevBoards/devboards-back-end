package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.Role;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.repositories.OrganizationUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationUserServiceTest {

    @Mock
    OrganizationService organizationService;

    @Mock
    OrganizationUserRepository organizationUserRepository;

    @InjectMocks
    OrganizationUserService organizationUserService;

    User user;
    User otherUser;
    Organization organization;
    Role role;

    @BeforeEach
    void setUp() {
        user = new User("testUser");
        otherUser = new User("otherUser");
        organization = new Organization(1, "newOrganization", user);
        organization.setUsers(new HashSet<>(Arrays.asList(new OrganizationUser(organization, user),
                new OrganizationUser(organization, otherUser))));
        role = new Role("role");
    }

    @Test
    void shouldListOrganizationUsers() {
        when(organizationService.isUserInOrganization(user, organization)).thenReturn(true);

        Set<OrganizationUser> organizationUsers = organizationUserService.getOrganizationUsers(user, organization);
        assertEquals(organization.getUsers(), organizationUsers);
    }

    @Test
    void listShouldThrow403WhenNotInOrganization() {
        when(organizationService.isUserInOrganization(user, organization)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationUserService.getOrganizationUsers(user, organization));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void shouldAddOrganizationUser() {
        User addedUser = new User("addedUser");
        OrganizationUser organizationUser = new OrganizationUser(organization, addedUser, role);

        organizationUserService.addOrganizationUser(user, organization, addedUser, role);

        verify(organizationUserRepository, times(1)).save(organizationUser);
    }

    @Test
    void addShouldThrow403IfNotOwner() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationUserService.addOrganizationUser(otherUser, organization, new User("test"), role));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void shouldThrow403IfAttemptingToRemoveOwner() {
        when(organizationUserRepository.findByUserAndOrganization(user, organization))
                .thenReturn(Optional.of(new OrganizationUser(organization, user)));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationUserService.removeOrganizationUser(user, organization, user));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void shouldRemoveUser() {
        User userToRemove = new User("userToRemove");
        OrganizationUser organizationUser = new OrganizationUser(organization, userToRemove);
        when(organizationUserRepository.findByUserAndOrganization(userToRemove, organization))
                .thenReturn(Optional.of(organizationUser));

        organizationUserService.removeOrganizationUser(user, organization, userToRemove);

        organization.getUsers().remove(organizationUser);
        verify(organizationService, times(1)).updateOrganization(organization);
    }

    @Test
    void removeShouldThrow404IfUserNotInOrganization() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationUserService.removeOrganizationUser(user, organization, new User("ttt")));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void shouldUpdateUser() {
        OrganizationUser organizationUser = organization.getUsers().stream()
                .filter(ou -> ou.getUser().equals(otherUser)).findFirst().orElseThrow(AssertionError::new);
        Role newRole = new Role("newRole");

        organizationUserService.updateOrganizationUser(user, organization, otherUser, newRole);

        organizationUser.setRole(newRole);
        verify(organizationUserRepository, times(1)).save(organizationUser);
    }

    @Test
    void updateShouldThrow404IfUserNotInOrganization() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationUserService.updateOrganizationUser(user, organization, new User("ttt"), role));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void shouldThrow403IfUserAttemptingToUpdateThemselves() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationUserService.updateOrganizationUser(otherUser, organization, otherUser, role));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }
}
