package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.Role;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.repositories.OrganizationUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

/**
 * Singleton service for interacting with organization users. Performs all user authorization checks before performing
 * any operations made with the context of a requesting user.
 */
@Service
public class OrganizationUserService {

    private final OrganizationService organizationService;
    private final OrganizationUserRepository organizationUserRepository;

    public OrganizationUserService(OrganizationService organizationService,
                                   OrganizationUserRepository organizationUserRepository) {
        this.organizationService = organizationService;
        this.organizationUserRepository = organizationUserRepository;
    }

    /**
     * Gets a list of an organization's users by its id.
     *
     * @param requestUser  The user making the request.
     * @param organization The organization to get users from.
     * @return The list of organization users.
     */
    public Set<OrganizationUser> getOrganizationUsers(User requestUser, Organization organization) {
        validateViewPermission(requestUser, organization);

        return organization.getUsers();
    }

    /**
     * Adds a user to an organization.
     *
     * @param requestUser  The user making the request.
     * @param organization The organization to add the user to.
     * @param newUser      The user to add to the organization.
     * @param role         The role to give the new user.
     */
    public void addOrganizationUser(User requestUser, Organization organization, User newUser, Role role) {
        OrganizationUser organizationUser = new OrganizationUser(organization, newUser, role);

        validateCreatePermission(requestUser, organizationUser);

        organizationUserRepository.save(organizationUser);
    }

    /**
     * Removes a user from an organization.
     *
     * @param requestUser  The user making the request.
     * @param organization The organization to remove the user from.
     * @param userToRemove The user to remove.
     */
    public void removeOrganizationUser(User requestUser, Organization organization, User userToRemove) {
        OrganizationUser organizationUser = organizationUserRepository
                .findByUserAndOrganization(userToRemove, organization)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not in organization."));

        validateUpdateOrDeletePermission(requestUser, organizationUser);

        organization.getUsers().remove(organizationUser);
        organizationService.updateOrganization(organization);
    }

    /**
     * Updates an organization user.
     *
     * @param requestUser  The user making the request.
     * @param organization The organization the user should belong to.
     * @param user         The user to update.
     * @param role         The new role of the user.
     */
    public void updateOrganizationUser(User requestUser, Organization organization, User user, Role role) {
        OrganizationUser organizationUser = organization.getUsers().stream()
                .filter(ou -> ou.getUser().equals(user)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not in organization."));

        validateUpdateOrDeletePermission(requestUser, organizationUser);

        organizationUser.setRole(role);
        organizationUserRepository.save(organizationUser);
    }

    /* Validates view permissions by checking if the request user is in the organization. */
    private void validateViewPermission(User requestUser, Organization organization) {
        if (!organizationService.isUserInOrganization(requestUser, organization)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to view users in this organization.");
        }
    }

    /* Validates create permissions by checking if the request user is the owner of the organization. */
    private void validateCreatePermission(User requestUser, OrganizationUser organizationUser) {
        if (!requestUser.equals(organizationUser.getOrganization().getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to add users to this organization.");
        }
    }

    /* Validates update or delete permission by making sure a user is not updating themselves, and they are the owner
    . */
    private void validateUpdateOrDeletePermission(User requestUser, OrganizationUser organizationUser) {
        if (!requestUser.equals(organizationUser.getOrganization().getOwner()) || requestUser.equals(organizationUser.getUser())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allow to update this organization user");
        }
    }
}
