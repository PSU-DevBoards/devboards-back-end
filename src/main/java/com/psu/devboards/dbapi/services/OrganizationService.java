package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.requests.OrganizationRequest;
import com.psu.devboards.dbapi.repositories.OrganizationRepository;
import com.psu.devboards.dbapi.utils.NameUniqueViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashSet;

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
     * Finds an organization by its id.
     *
     * @param requestUser The requesting user.
     * @param id          The id of the organization to find.
     * @return The found organization.
     */
    public Organization findOrganizationById(User requestUser, Integer id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found."));

        validateViewPermission(requestUser, organization);

        return organization;
    }

    /**
     * Creates a new organization and makes the requesting user an owner. Additionally, makes the requesting user
     * a member of the new organization.
     *
     * @param requestUser The requesting user.
     * @param name        The name of the new organization.
     * @return The created organization.
     */
    public Organization createOrganization(User requestUser, String name) {
        Organization organization = new Organization(name, requestUser);
        organization.setUsers(new HashSet<>(Collections.singletonList(new OrganizationUser(organization, requestUser))));

        if( organizationRepository.findByName(name).isPresent() ){
            throw new NameUniqueViolationException();
        }

        return organizationRepository.save(organization);
    }

    /**
     * Updates an organization by its id.
     *
     * @param requestUser         The user making the request.
     * @param id                  The id of the organization to update.
     * @param organizationRequest The attributes to update.
     * @return The updated organization.
     */
    public Organization updateOrganizationById(User requestUser, Integer id, OrganizationRequest organizationRequest) {
        Organization organization = findOrganizationById(requestUser, id);
        validateUpdatePermission(requestUser, organization);

        organization.setName(organizationRequest.getName());
        return organizationRepository.save(organization);
    }

    /**
     * Updates an organization without checking if a user has permission to update it.
     *
     * @param organization The organization to update.
     * @return The updated organization.
     */
    public Organization updateOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    /**
     * Deletes an organization by its id.
     *
     * @param requestUser The user making the request.
     * @param id          The id of the organization to delete.
     */
    public void deleteOrganizationById(User requestUser, Integer id) {
        Organization organization = findOrganizationById(requestUser, id);
        validateDeletePermission(requestUser, organization);

        organizationRepository.delete(organization);
    }

    /**
     * Checks to see if a user is in an organization.
     *
     * @param user         The user to check for.
     * @param organization The organization to check in.
     * @return If the user is in the organization or not.
     */
    public boolean isUserInOrganization(User user, Organization organization) {
        return organization.getUsers().stream()
                .anyMatch(organizationUser -> organizationUser.getUser().equals(user));
    }

    /* Validates that the user entity has authorizations to view the organization entity */
    private void validateViewPermission(User user, Organization organization) {
        if (!isUserInOrganization(user, organization)) {
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
