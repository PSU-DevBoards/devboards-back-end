package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.requests.OrganizationRequest;
import com.psu.devboards.dbapi.repositories.OrganizationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization findOrganization(User user, Integer id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found."));

        validateViewPermission(user, organization);

        return organization;
    }

    public Organization createOrganization(User user, String name) {
        Organization organization = new Organization(name, user, Collections.singletonList(user));

        return organizationRepository.save(organization);
    }

    public Organization updateOrganization(User user, Integer id, OrganizationRequest organizationRequest) {
        Organization organization = findOrganization(user, id);
        validateUpdatePermission(user, organization);

        organization.setName(organizationRequest.getName());
        return organizationRepository.save(organization);
    }

    public void deleteOrganization(User user, Integer id) {
        Organization organization = findOrganization(user, id);
        validateDeletePermission(user, organization);

        organizationRepository.delete(organization);
    }

    private void validateViewPermission(User user, Organization organization) {
        if(!organization.getUsers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not allowed to view this organization.");
        }
    }

    private void validateUpdatePermission(User user, Organization organization) {
        if (!user.equals(organization.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not allowed to update this organization.");
        }
    }

    private void validateDeletePermission(User user, Organization organization) {
        if (!user.equals(organization.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not allowed to delete this organization.");
        }
    }
}
