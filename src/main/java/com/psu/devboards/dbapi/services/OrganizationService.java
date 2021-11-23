package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.exceptions.UniqueViolationException;
import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.Role;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.requests.OrganizationRequest;
import com.psu.devboards.dbapi.repositories.OrganizationRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;

/**
 * Singleton service for interacting with organizations.
 */
@Service
public class OrganizationService extends CrudService<Integer, Organization, OrganizationRequest> {

    private final UserService userService;
    private final RoleService roleService;

    public OrganizationService(OrganizationRepository organizationRepository, UserService userService,
                               RoleService roleService) {
        super(organizationRepository);
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    protected Organization updateEntityFromRequest(OrganizationRequest request, Organization entity) {
        if (request.getName() != null && !entity.getName().equals(request.getName())) {
            validateUniqueOrganization(request);
            entity.setName(request.getName());
        }

        return entity;
    }

    @Override
    protected Organization createEntityFromRequest(OrganizationRequest request) {
        validateUniqueOrganization(request);

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByUserName(principal.getName());

        Role role = roleService.getByName("Scrum Master");

        Organization organization = new Organization(request.getName(), user);
        organization.setUsers(new HashSet<>(Collections.singletonList(new OrganizationUser(organization, user, role))));

        return organization;
    }

    private void validateUniqueOrganization(OrganizationRequest request) throws UniqueViolationException {
        ((OrganizationRepository) repository).findByName(request.getName())
                .ifPresent(organization -> {
                    throw new UniqueViolationException("Organization", "name");
                });
    }
}
