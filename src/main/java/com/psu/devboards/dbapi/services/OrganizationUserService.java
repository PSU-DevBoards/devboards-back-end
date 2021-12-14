package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.OrganizationUserKey;
import com.psu.devboards.dbapi.models.entities.Role;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.requests.OrganizationUserRequest;
import com.psu.devboards.dbapi.repositories.OrganizationUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * Singleton service for interacting with organization users.
 */
@Service
public class OrganizationUserService extends CrudService<OrganizationUserKey, OrganizationUser,
        OrganizationUserRequest> {

    private final OrganizationService organizationService;
    private final RoleService roleService;
    private final UserService userService;

    public OrganizationUserService(OrganizationService organizationService, RoleService roleService,
                                   OrganizationUserRepository organizationUserRepository, UserService userService) {
        super(organizationUserRepository);
        this.organizationService = organizationService;
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * Gets a list of an organization's users by its id.
     *
     * @param organizationId The organization id to get users from.
     * @return The list of organization users.
     */
    public Set<OrganizationUser> getOrganizationUsers(Integer organizationId) {
        return organizationService.getById(organizationId).getUsers();
    }

    @Override
    protected OrganizationUser updateEntityFromRequest(OrganizationUserRequest request, OrganizationUser entity) {
        Optional.ofNullable(request.getRoleId()).ifPresent(roleId -> {
            Role role = roleService.getById(request.getRoleId());
            entity.setRole(role);
        });

        return entity;
    }

    @Override
    protected OrganizationUser createEntityFromRequest(OrganizationUserRequest request) {
        Role role = roleService.getById(request.getRoleId());

        // Get a user by their email or create a user with just with email for an invitation type situation.
        User user = userService.findByEmail(request.getEmail()).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(request.getEmail());
            return userService.saveUser(newUser);
        });

        Organization organization = organizationService.getById(request.getOrganizationId());

        return new OrganizationUser(organization, user, role);
    }
}
