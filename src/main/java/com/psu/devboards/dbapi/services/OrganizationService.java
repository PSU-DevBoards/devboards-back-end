package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.requests.OrganizationRequest;
import com.psu.devboards.dbapi.repositories.OrganizationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Set;

/**
 * Singleton service for interacting with organizations. Performs all user authorization checks before performing
 * any operations made with the context of a requesting user.
 */
@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    /**
     * Finds an organization by it's id.
     *
     * @param user The requesting user.
     * @param id   The id of the organization to find.
     * @return The found organization.
     */
    public Organization findOrganizationById(User user, Integer id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found."));

        validateViewPermission(user, organization);

        return organization;
    }

    /**
     * Creates a new organization and makes the requesting user an owner. Additionally, makes the requesting user
     * a member of the new organization.
     *
     * @param user The requesting user.
     * @param name The name of the new organization.
     * @return The created organization.
     */
    public Organization createOrganization(User user, String name) {
        Organization organization = new Organization(name, user, Collections.singleton(user));

        return organizationRepository.save(organization);
    }

    /**
     * Updates an organization by it's id.
     *
     * @param user                The user making the request.
     * @param id                  The id of the organization to update.
     * @param organizationRequest The attributes to update.
     * @return The updated organization.
     */
    public Organization updateOrganizationById(User user, Integer id, OrganizationRequest organizationRequest) {
        Organization organization = findOrganizationById(user, id);
        validateUpdatePermission(user, organization);

        organization.setName(organizationRequest.getName());
        return organizationRepository.save(organization);
    }

    /**
     * Deletes an organization by it's id.
     *
     * @param user The user making the request.
     * @param id   The id of the organization to delete.
     */
    public void deleteOrganizationById(User user, Integer id) {
        Organization organization = findOrganizationById(user, id);
        validateDeletePermission(user, organization);

        organizationRepository.delete(organization);
    }

    /**
     * Gets a list of an organization's users by it's id.
     *
     * @param user The user making the request.
     * @param id   The id of the organization.
     * @return The list of organization users.
     */
    public Set<User> getOrganizationUsers(User user, Integer id) {
        Organization organization = findOrganizationById(user, id);

        return organization.getUsers();
    }

    /**
     * Adds a user to an organization.
     *
     * @param user           The user making the request.
     * @param organizationId The id of the organization to add the user to.
     * @param newUser        The user to add to the organization.
     */
    public void addOrganizationUser(User user, Integer organizationId, User newUser) {
        Organization organization = findOrganizationById(user, organizationId);
        validateUpdatePermission(user, organization);

        organization.getUsers().add(newUser);
        organizationRepository.save(organization);
    }

    /**
     * Removes a user from an organization.
     *
     * @param user           The user making the request.
     * @param organizationId The id of the organization to remove the user from.
     * @param userToRemove   The user to remove.
     */
    public void removeOrganizationUser(User user, Integer organizationId, User userToRemove) {
        Organization organization = findOrganizationById(user, organizationId);
        if (userToRemove.equals(organization.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to remove the organization owner.");
        }

        validateUpdatePermission(user, organization);

        organization.getUsers().remove(userToRemove);
        organizationRepository.save(organization);
    }

    /* Validates that the user entity has authorizations to view the organization entity */
    private void validateViewPermission(User user, Organization organization) {
        if (!organization.getUsers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not allowed to view this organization.");
        }
    }

    /* Validates that the user entity has authorizations to update the organization entity */
    private void validateUpdatePermission(User user, Organization organization) {
        if (!user.equals(organization.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not allowed to update this organization.");
        }
    }

    /* Validates that the user entity has authorizations to delete the organization entity */
    private void validateDeletePermission(User user, Organization organization) {
        if (!user.equals(organization.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not allowed to delete this organization.");
        }
    }
}
